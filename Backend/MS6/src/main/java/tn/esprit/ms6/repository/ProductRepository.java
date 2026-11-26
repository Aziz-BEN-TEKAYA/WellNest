package tn.esprit.ms6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.ms6.entity.Product;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    List<Product> findByCategoryId(Long categoryId);

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByCategoryIdAndNameContainingIgnoreCase(Long categoryId, String keyword);

    boolean existsByCategoryId(Long categoryId);

    /**
     * Reservation ATOMIQUE du stock : une seule requete qui retire la quantite
     * seulement s'il en reste assez. Renvoie 1 si reussi, 0 si stock insuffisant.
     * Deux clients ne peuvent donc pas acheter le dernier produit en meme temps.
     * A appeler dans une methode @Transactional.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Product p set p.stock = p.stock - :quantity where p.id = :id and p.stock >= :quantity")
    int reserveStock(@Param("id") Long id, @Param("quantity") int quantity);

    /** Rend des unites au stock (annulation, paiement echoue). */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Product p set p.stock = p.stock + :quantity where p.id = :id")
    int releaseStock(@Param("id") Long id, @Param("quantity") int quantity);
}
