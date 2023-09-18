package com.acorn.acornstore.web.controller;

import com.acorn.acornstore.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{userId}/checkout")
    public ResponseEntity<Void> checkout(@PathVariable Long userId, @RequestParam int userBudget) {
        try {
            cartService.checkout(userId, userBudget);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        try {
            cartService.addToCart(userId, productId, quantity);
            return ResponseEntity.ok("Product added to cart successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeProductFromCart(@RequestParam Long userId, @RequestParam Long productId) {
        try {
            cartService.removeProductFromCard(userId, productId);
            return ResponseEntity.ok("Product removed from cart successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}