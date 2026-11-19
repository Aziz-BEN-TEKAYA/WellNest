package tn.esprit.ms6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.ms6.entity.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByBuyerUserId(String buyerUserId);

    List<Order> findByBeneficiaryUserId(String beneficiaryUserId);
}
