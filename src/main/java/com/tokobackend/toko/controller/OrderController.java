package com.tokobackend.toko.controller;

import com.tokobackend.toko.model.Order;
import com.tokobackend.toko.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(order -> new ResponseEntity<>(order, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Order order) {
        try {
            Order savedOrder = orderService.createOrder(order);
            return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Tangani error seperti user/product tidak ditemukan, stok tidak cukup
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // Mengirim pesan error
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            Order updatedOrder = orderService.updateOrderStatus(id, status);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        try {
            List<Order> orders = orderService.getOrdersByUserId(userId);
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // User not found
        }
    }
}
