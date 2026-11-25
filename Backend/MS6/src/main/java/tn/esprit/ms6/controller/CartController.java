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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.ms6.dto.CartItemRequest;
import tn.esprit.ms6.dto.CartItemResponse;
import tn.esprit.ms6.dto.CartItemUpdateRequest;
import tn.esprit.ms6.dto.CartRequest;
import tn.esprit.ms6.dto.CartResponse;
import tn.esprit.ms6.service.CartService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/store/carts")
@RequiredArgsConstructor
@Tag(name = "Paniers", description = "Paniers et lignes de panier")
public class CartController {

    private final CartService cartService;

    // ----- Paniers -----

    @PostMapping
    public ResponseEntity<CartResponse> create(@Valid @RequestBody CartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.create(request));
    }

    @GetMapping
    public CartResponse findByUserId(@RequestParam String userId) {
        return cartService.findByUserId(userId);
    }

    @GetMapping("/{id}")
    public CartResponse findById(@PathVariable Long id) {
        return cartService.findById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        cartService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ----- Lignes du panier -----

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemResponse> addItem(@PathVariable Long cartId,
                                                    @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cartService.addItem(cartId, request));
    }

    @GetMapping("/{cartId}/items")
    public List<CartItemResponse> findItems(@PathVariable Long cartId) {
        return cartService.findItems(cartId);
    }

    @PutMapping("/{cartId}/items/{itemId}")
    public CartItemResponse updateItem(@PathVariable Long cartId, @PathVariable Long itemId,
                                       @Valid @RequestBody CartItemUpdateRequest request) {
        return cartService.updateItem(cartId, itemId, request);
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long cartId, @PathVariable Long itemId) {
        cartService.deleteItem(cartId, itemId);
        return ResponseEntity.noContent().build();
    }
}