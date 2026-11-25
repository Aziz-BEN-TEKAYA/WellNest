package tn.esprit.ms6.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.ms6.dto.ProductRequest;
import tn.esprit.ms6.dto.ProductResponse;
import tn.esprit.ms6.dto.StockAdjustmentRequest;
import tn.esprit.ms6.enums.AutonomyNeed;
import tn.esprit.ms6.enums.Obstacle;
import tn.esprit.ms6.enums.ProductSort;
import tn.esprit.ms6.enums.UsageDuration;
import tn.esprit.ms6.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/store/products")
@RequiredArgsConstructor
@Tag(name = "Produits", description = "Catalogue des équipements d'autonomie et de mobilité")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(request));
    }

    @GetMapping
    public List<ProductResponse> findAll(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false) UsageDuration usageDuration,
            @RequestParam(required = false) Obstacle obstacle,
            @RequestParam(required = false) AutonomyNeed need,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) ProductSort sort) {
        return productService.findAll(categoryId, minPrice, maxPrice, available,
                usageDuration, obstacle, need, keyword, sort);
    }

    @GetMapping("/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductRequest request) {
        return productService.update(id, request);
    }

    @PatchMapping("/{id}/stock")
    public ProductResponse adjustStock(@PathVariable Long id,
                                       @Valid @RequestBody StockAdjustmentRequest request) {
        return productService.adjustStock(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}