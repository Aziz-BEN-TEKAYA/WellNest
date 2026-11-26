package tn.esprit.ms6.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.ms6.dto.ProductRequest;
import tn.esprit.ms6.dto.ProductResponse;
import tn.esprit.ms6.dto.StockAdjustmentRequest;
import tn.esprit.ms6.entity.Category;
import tn.esprit.ms6.entity.Product;
import tn.esprit.ms6.enums.AutonomyNeed;
import tn.esprit.ms6.enums.Obstacle;
import tn.esprit.ms6.enums.OrderStatus;
import tn.esprit.ms6.enums.ProductSort;
import tn.esprit.ms6.enums.UsageDuration;
import tn.esprit.ms6.exception.BadRequestException;
import tn.esprit.ms6.exception.ConflictException;
import tn.esprit.ms6.exception.ResourceNotFoundException;
import tn.esprit.ms6.mapper.ProductMapper;
import tn.esprit.ms6.repository.CartItemRepository;
import tn.esprit.ms6.repository.OrderItemRepository;
import tn.esprit.ms6.repository.ProductRepository;
import tn.esprit.ms6.repository.ProductSpecifications;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    /** Seules ces commandes comptent pour la popularité (pas les annulées ni les paiements échoués). */
    private static final List<OrderStatus> COUNTED_STATUSES =
            List.of(OrderStatus.PAID, OrderStatus.SHIPPED, OrderStatus.DELIVERED);

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;

    @Transactional
    public ProductResponse create(ProductRequest request) {
        validateDimensions(request);
        Category category = categoryService.getEntity(request.categoryId());
        Product saved = productRepository.save(productMapper.toEntity(request, category));
        return productMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice,
                                         Boolean available, UsageDuration usageDuration,
                                         Obstacle obstacle, AutonomyNeed need,
                                         String keyword, ProductSort sort) {
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new BadRequestException("Le prix minimum ne peut pas dépasser le prix maximum");
        }

        Specification<Product> spec = ProductSpecifications.all();
        if (categoryId != null) {
            spec = spec.and(ProductSpecifications.inCategory(categoryId));
        }
        if (minPrice != null) {
            spec = spec.and(ProductSpecifications.priceAtLeast(minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and(ProductSpecifications.priceAtMost(maxPrice));
        }
        if (available != null) {
            spec = spec.and(ProductSpecifications.availability(available));
        }
        if (usageDuration != null) {
            spec = spec.and(ProductSpecifications.usageDuration(usageDuration));
        }
        if (obstacle != null) {
            spec = spec.and(ProductSpecifications.withObstacle(obstacle));
        }
        if (need != null) {
            spec = spec.and(ProductSpecifications.withNeed(need));
        }
        if (keyword != null && !keyword.isBlank()) {
            spec = spec.and(ProductSpecifications.matchesKeyword(keyword));
        }

        ProductSort order = (sort == null) ? ProductSort.NEWEST : sort;
        List<Product> products = productRepository.findAll(spec, toSort(order));
        if (order == ProductSort.POPULARITY) {
            products = sortByPopularity(products);
        }
        return products.stream().map(productMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        return productMapper.toResponse(getEntity(id));
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        validateDimensions(request);
        Product product = getEntity(id);
        Category category = categoryService.getEntity(request.categoryId());
        productMapper.updateEntity(product, request, category);
        return productMapper.toResponse(productRepository.save(product));
    }

    /** Ajustement du stock : delta positif = on ajoute, négatif = on retire (jamais sous zéro). */
    @Transactional
    public ProductResponse adjustStock(Long id, StockAdjustmentRequest request) {
        getEntity(id); // 404 si le produit n'existe pas
        int delta = request.delta();
        if (delta == 0) {
            throw new BadRequestException("La variation de stock ne peut pas être nulle");
        }
        if (delta > 0) {
            productRepository.releaseStock(id, delta);
        } else if (productRepository.reserveStock(id, -delta) == 0) {
            throw new BadRequestException("Stock insuffisant : impossible de retirer " + (-delta) + " unité(s)");
        }
        // On relit le produit : les requêtes de stock vident le cache de la session.
        return productMapper.toResponse(getEntity(id));
    }

    @Transactional
    public void delete(Long id) {
        Product product = getEntity(id);
        if (orderItemRepository.existsByProductId(id)) {
            throw new ConflictException("Impossible de supprimer le produit « " + product.getName()
                    + " » : il figure dans une commande");
        }
        cartItemRepository.deleteByProductId(id); // un panier est un brouillon : on le nettoie
        productRepository.delete(product);
    }

    /** Réutilisée par CartService et OrderService pour retrouver un produit. */
    @Transactional(readOnly = true)
    public Product getEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produit introuvable (id = " + id + ")"));
    }

    // ----- méthodes privées -----

    private void validateDimensions(ProductRequest request) {
        if (request.foldedWidthCm() != null && request.widthCm() != null
                && request.foldedWidthCm().compareTo(request.widthCm()) > 0) {
            throw new BadRequestException("La largeur pliée ne peut pas dépasser la largeur du produit");
        }
    }

    private Sort toSort(ProductSort order) {
        return switch (order) {
            case PRICE_ASC -> Sort.by("price").ascending();
            case PRICE_DESC -> Sort.by("price").descending();
            case NEWEST, POPULARITY -> Sort.by("createdAt").descending();
        };
    }

    /** Du plus vendu au moins vendu ; à égalité, ordre alphabétique. */
    private List<Product> sortByPopularity(List<Product> products) {
        Map<Long, Long> sold = orderItemRepository.findSoldQuantities(COUNTED_STATUSES).stream()
                .collect(Collectors.toMap(
                        OrderItemRepository.ProductSales::getProductId,
                        OrderItemRepository.ProductSales::getTotal));
        return products.stream()
                .sorted(Comparator.comparingLong((Product p) -> sold.getOrDefault(p.getId(), 0L))
                        .reversed()
                        .thenComparing(Product::getName))
                .toList();
    }
}