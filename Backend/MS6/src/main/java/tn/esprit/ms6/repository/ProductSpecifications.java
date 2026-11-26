package tn.esprit.ms6.repository;

import org.springframework.data.jpa.domain.Specification;
import tn.esprit.ms6.entity.Product;
import tn.esprit.ms6.enums.AutonomyNeed;
import tn.esprit.ms6.enums.Obstacle;
import tn.esprit.ms6.enums.UsageDuration;

import java.math.BigDecimal;
import java.util.Set;

public final class ProductSpecifications {

    private ProductSpecifications() {
    }

    /** Point de départ : aucun filtre (tout passe). */
    public static Specification<Product> all() {
        return (root, query, cb) -> cb.conjunction();
    }

    public static Specification<Product> inCategory(Long categoryId) {
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> priceAtLeast(BigDecimal minPrice) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.<BigDecimal>get("price"), minPrice);
    }

    public static Specification<Product> priceAtMost(BigDecimal maxPrice) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.<BigDecimal>get("price"), maxPrice);
    }

    /** available = true : en stock ; available = false : en rupture. */
    public static Specification<Product> availability(boolean available) {
        return (root, query, cb) -> available
                ? cb.greaterThan(root.<Integer>get("stock"), 0)
                : cb.equal(root.<Integer>get("stock"), 0);
    }

    /** Un produit BOTH convient aussi bien à un usage temporaire qu'à long terme. */
    public static Specification<Product> usageDuration(UsageDuration duration) {
        return (root, query, cb) -> cb.or(
                cb.equal(root.get("usageDuration"), duration),
                cb.equal(root.get("usageDuration"), UsageDuration.BOTH));
    }

    public static Specification<Product> withObstacle(Obstacle obstacle) {
        return (root, query, cb) -> cb.isMember(obstacle, root.<Set<Obstacle>>get("obstacles"));
    }

    public static Specification<Product> withNeed(AutonomyNeed need) {
        return (root, query, cb) -> cb.isMember(need, root.<Set<AutonomyNeed>>get("needs"));
    }

    public static Specification<Product> matchesKeyword(String keyword) {
        String pattern = "%" + keyword.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.<String>get("name")), pattern),
                cb.like(cb.lower(root.<String>get("description")), pattern));
    }
}