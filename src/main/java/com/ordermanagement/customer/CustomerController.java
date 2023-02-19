package com.ordermanagement.customer;

import com.ordermanagement.order.Order;
import com.ordermanagement.order.OrderDto;
import com.ordermanagement.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final OrderService orderService;
    @Autowired
    public CustomerController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        try {
            return ResponseEntity.ok(customerService.createCustomer(customer));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Object> deleteCustomerByCode(@PathVariable("code") Long code) {
        try {
            customerService.deleteCustomerByCode(code);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{code}")
    public ResponseEntity<Customer> findCustomerByCode(@PathVariable("code") Long code) {
        Optional<Customer> customer = customerService.findCustomerByCode(code);
        return customer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{code}/orders")
    public ResponseEntity<List<OrderDto>> findOrdersByCustomerCode(@PathVariable("code") Long code) {
        return ResponseEntity.ok(
                orderService.findOrdersByCustomer(code)
                        .stream()
                        .map(Order::toDto)
                        .toList()
        );
    }
}
