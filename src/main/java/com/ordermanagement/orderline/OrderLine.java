package com.ordermanagement.orderline;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ordermanagement.order.Order;
import com.ordermanagement.product.Product;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "order_line")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_sku_code", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;
}
