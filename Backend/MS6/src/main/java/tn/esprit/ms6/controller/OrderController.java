package tn.esprit.ms6.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.ms6.dto.OrderItemResponse;
import tn.esprit.ms6.dto.OrderRequest;
import tn.esprit.ms6.dto.OrderResponse;
import tn.esprit.ms6.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store/orders")
@RequiredArgsConstructor
@Tag(name = "Commandes", description = "Commandes créées depuis le panier")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request));
    }

    @GetMapping
    public List<OrderResponse> findAll(@RequestParam(required = false) String buyerUserId,
                                       @RequestParam(required = false) String beneficiaryUserId) {
        return orderService.findAll(buyerUserId, beneficiaryUserId);
    }

    @GetMapping("/{id}")
    public OrderResponse findById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping("/{id}/items")
    public List<OrderItemResponse> findItems(@PathVariable Long id) {
        return orderService.findItems(id);
    }

    /** Annulation (pas une suppression) : renvoie la commande avec le statut CANCELLED. */
    @DeleteMapping("/{id}")
    public OrderResponse cancel(@PathVariable Long id) {
        return orderService.cancel(id);
    }
}