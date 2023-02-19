package com.ordermanagement.order;

import com.ordermanagement.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/")
public class OrderController {
    private final OrderService orderService;
    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public ResponseEntity<Object> createOrder(@RequestBody OrderCreateRequest orderCreateRequest) {
        try {
            Order order = orderService.createOrder(orderCreateRequest);
            return ResponseEntity.ok(order.toDto());
        } catch (Exception e) {
            if(e instanceof NotFoundException)
                return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/order/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable("id") Long id) {
        try {
            orderService.deleteOrderById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderDto> findOrderById(@PathVariable("id") Long id) {
        Optional<Order> order = orderService.findOrderById(id);
        return order.map(value -> ResponseEntity.ok(value.toDto()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> findOrdersByDate(@RequestBody LocalDate date) {
        return ResponseEntity.ok(
                orderService.findOrdersByDate(date)
                        .stream()
                        .map(Order::toDto)
                        .toList()
        );
    }
}
