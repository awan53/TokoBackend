package com.tokobackend.toko.controller;
import com.tokobackend.toko.model.OrderItem;
import com.tokobackend.toko.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping
    public ResponseEntity<List<OrderItem>> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemService.getAllOrderItems();
        return new ResponseEntity<>(orderItems, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long id) {
        return orderItemService.getOrderItemById(id)
                .map(item -> new ResponseEntity<>(item, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<OrderItem> createOrderItem(@RequestBody OrderItem orderItem) {
        // Perhatikan bahwa OrderItem biasanya dibuat bersamaan dengan Order,
        // endpoint ini mungkin jarang digunakan secara independen.
        // Validasi order dan product di sini atau di service jika diperlukan.
        OrderItem savedItem = orderItemService.saveOrderItem(orderItem);
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
