package com.tokobackend.toko.controller;

import com.tokobackend.toko.model.CartItem;
import com.tokobackend.toko.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-item")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @GetMapping("/id")
    public ResponseEntity<CartItem> getCartItemByid(@PathVariable Long id){
        return  cartItemService.getCartItemById(id).map(item-> new ResponseEntity<>(item, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/id")
    public ResponseEntity<CartItem> AddorUpdateCarItem(@RequestParam Long userId, @RequestParam Long productId, @RequestParam Integer quantity){
        try {
            CartItem savedItem = cartItemService.addOrUpdateCartItem(userId, productId, quantity);
            return new ResponseEntity<>(savedItem, HttpStatus.OK); // OK karena bisa update atau create
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // User/Product not found
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
        cartItemService.deleteCartItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartItem>> getCartItemsByUserId(@PathVariable Long userId) {
        try {
            List<CartItem> cartItems = cartItemService.getCartItemsByUserId(userId);
            return new ResponseEntity<>(cartItems, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // User not found
        }
    }

    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<Void> clearCartByUserId(@PathVariable Long userId) {
        try {
            cartItemService.clearCartByUserId(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // User not found
        }
    }



}
