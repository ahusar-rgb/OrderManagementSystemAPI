package com.ordermanagement.order;

import com.ordermanagement.customer.Customer;
import com.ordermanagement.orderline.OrderLine;
import com.ordermanagement.orderline.OrderLineDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_registration_number", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<OrderLine> orderLines;

    @Column(name = "date_of_submission", nullable = false)
    private LocalDate dateOfSubmission;

    public OrderDto toDto() {
        return new OrderDto(
                this.getId(),
                this.getCustomer().getRegistrationCode(),
                this.getDateOfSubmission(),
                this.getOrderLines().stream().map(orderLine -> new OrderLineDto(
                        orderLine.getId(),
                        orderLine.getProduct().getSkuCode(),
                        orderLine.getQuantity()
                )).toList()
        );
    }
}
