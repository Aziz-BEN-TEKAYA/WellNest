package tn.esprit.ms6.mapper;

import org.springframework.stereotype.Component;
import tn.esprit.ms6.dto.OrderItemResponse;
import tn.esprit.ms6.dto.OrderResponse;
import tn.esprit.ms6.entity.Order;
import tn.esprit.ms6.entity.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class OrderMapper {

    public OrderItemResponse toItemResponse(OrderItem item) {
        BigDecimal lineTotal = item.getUnitPrice()
                .multiply(BigDecimal.valueOf(item.getQuantity()))
                .setScale(3, RoundingMode.HALF_UP);
        return new OrderItemResponse(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getUnitPrice(),
                lineTotal
        );
    }

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        return new OrderResponse(
                order.getId(),
                order.getBuyerUserId(),
                order.getBeneficiaryUserId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus(),
                items
        );
    }
}