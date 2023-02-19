package com.ordermanagement.orderline;

import com.ordermanagement.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order-line")
public class OrderLineController {
    private final OrderLineService orderLineService;

    public OrderLineController(OrderLineService orderLineService) {
        this.orderLineService = orderLineService;
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> updateQuantity(@PathVariable("id") Long id, @RequestBody Integer quantity) {
        try {
            orderLineService.updateQuantity(id, quantity);
        } catch (Exception e) {
            if (e instanceof NotFoundException)
                return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }
}
