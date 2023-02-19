package com.ordermanagement.product;

import com.ordermanagement.order.Order;
import com.ordermanagement.order.OrderDto;
import com.ordermanagement.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {
    private final ProductService productService;
    private final OrderService orderService;
    @Autowired
    public ProductController(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        try {
            return ResponseEntity.ok(productService.saveProduct(product));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProductBySku(@PathVariable("id") String skuCode) {
        try {
            productService.deleteProductBySku(skuCode);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findProductBySku(@PathVariable("id") String skuCode) {
        return productService.findProductBySkuCode(skuCode)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{skuCode}/orders")
    public ResponseEntity<List<OrderDto>> findOrdersByProductSku(@PathVariable("skuCode") String skuCode) {
        return ResponseEntity.ok(
                orderService.findOrdersByProductSku(skuCode)
                        .stream()
                        .map(Order::toDto)
                        .toList()
        );
    }
}
