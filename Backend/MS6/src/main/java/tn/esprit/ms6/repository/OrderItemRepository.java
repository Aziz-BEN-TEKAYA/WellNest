package tn.esprit.ms6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.ms6.entity.OrderItem;
import tn.esprit.ms6.enums.OrderStatus;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    boolean existsByProductId(Long productId);

    /** Quantités vendues par produit, pour les commandes dont le statut est dans la liste. */
    @Query("""
            select oi.product.id as productId, sum(oi.quantity) as total
            from OrderItem oi
            where oi.order.status in :statuses
            group by oi.product.id
            """)
    List<ProductSales> findSoldQuantities(@Param("statuses") Collection<OrderStatus> statuses);

    interface ProductSales {
        Long getProductId();

        Long getTotal();
    }
}