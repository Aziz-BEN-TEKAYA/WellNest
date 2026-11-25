package tn.esprit.ms6.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms6.dto.ProductRequest;
import tn.esprit.ms6.dto.ProductResponse;
import tn.esprit.ms6.entity.Category;
import tn.esprit.ms6.entity.Product;
import tn.esprit.ms6.enums.UsageDuration;

import java.math.RoundingMode;
import java.util.HashSet;
import java.util.TreeSet;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request, Category category) {
        return Product.builder()
                .name(request.name().trim())
                .description(request.description())
                .price(request.price().setScale(3, RoundingMode.HALF_UP))
                .stock(request.stock())
                .image(request.image())
                .weightKg(request.weightKg())
                .widthCm(request.widthCm())
                .heightCm(request.heightCm())
                .depthCm(request.depthCm())
                .foldedWidthCm(request.foldedWidthCm())
                .minimumDoorWidthCm(request.minimumDoorWidthCm())
                .material(request.material())
                .warrantyMonths(request.warrantyMonths())
                .maxLoadKg(request.maxLoadKg())
                .installationRequired(Boolean.TRUE.equals(request.installationRequired()))
                .usageDuration(request.usageDuration() != null ? request.usageDuration() : UsageDuration.BOTH)
                .obstacles(request.obstacles() == null ? new HashSet<>() : new HashSet<>(request.obstacles()))
                .needs(request.needs() == null ? new HashSet<>() : new HashSet<>(request.needs()))
                .category(category)
                .build();
    }

    public void updateEntity(Product product, ProductRequest request, Category category) {
        product.setName(request.name().trim());
        product.setDescription(request.description());
        product.setPrice(request.price().setScale(3, RoundingMode.HALF_UP));
        product.setStock(request.stock());
        product.setImage(request.image());
        product.setWeightKg(request.weightKg());
        product.setWidthCm(request.widthCm());
        product.setHeightCm(request.heightCm());
        product.setDepthCm(request.depthCm());
        product.setFoldedWidthCm(request.foldedWidthCm());
        product.setMinimumDoorWidthCm(request.minimumDoorWidthCm());
        product.setMaterial(request.material());
        product.setWarrantyMonths(request.warrantyMonths());
        product.setMaxLoadKg(request.maxLoadKg());
        product.setInstallationRequired(Boolean.TRUE.equals(request.installationRequired()));
        product.setUsageDuration(request.usageDuration() != null ? request.usageDuration() : UsageDuration.BOTH);
        product.setCategory(category);

        product.getObstacles().clear();
        if (request.obstacles() != null) {
            product.getObstacles().addAll(request.obstacles());
        }
        product.getNeeds().clear();
        if (request.needs() != null) {
            product.getNeeds().addAll(request.needs());
        }
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImage(),
                product.getWeightKg(),
                product.getWidthCm(),
                product.getHeightCm(),
                product.getDepthCm(),
                product.getFoldedWidthCm(),
                product.getMinimumDoorWidthCm(),
                product.getMaterial(),
                product.getWarrantyMonths(),
                product.getMaxLoadKg(),
                product.isInstallationRequired(),
                product.getUsageDuration(),
                product.getCreatedAt(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                new TreeSet<>(product.getObstacles()),
                new TreeSet<>(product.getNeeds())
        );
    }
}