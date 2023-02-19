package com.ordermanagement.order;

import com.ordermanagement.customer.Customer;
import com.ordermanagement.exception.NotFoundException;
import com.ordermanagement.orderline.OrderLine;
import com.ordermanagement.orderline.OrderLineCreateRequest;
import com.ordermanagement.product.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {
    @Mock
    private OrderService orderService;
    @InjectMocks
    private OrderController orderController;

    @Test
    @DisplayName("It should return OK when [create order]")
    void itShouldReturnOkWhenCreateOrder() {
        List<OrderLine> orderLines = List.of(
                OrderLine.builder()
                        .id(1L)
                        .product(Product.builder()
                                .skuCode("skuCode")
                                .name("name")
                                .unitPrice(1F)
                                .build())
                        .quantity(1)
                        .build()
        );
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                1L,
                1234L,
                LocalDate.of(2021, 1, 1),
                List.of(
                        new OrderLineCreateRequest("skuCode", 1, 1L)
                )
        );
        Order order = Order.builder()
                .id(orderCreateRequest.id())
                .customer(Customer.builder()
                        .registrationCode(1L)
                        .email("email")
                        .fullName("customer")
                        .telephone("telephone")
                        .build())
                .dateOfSubmission(orderCreateRequest.dateOfSubmission())
                .orderLines(orderLines)
                .build();
        when(orderService.createOrder(orderCreateRequest)).thenReturn(order);
        ResponseEntity<Object> responseEntity = orderController.createOrder(orderCreateRequest);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(order.toDto(), responseEntity.getBody());
    }

    @Test
    @DisplayName("It should return NOT_FOUND when [create order] and customer not found")
    void itShouldReturnNotFoundWhenCreateOrderAndCustomerNotFound() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                1L,
                1234L,
                LocalDate.of(2021, 1, 1),
                List.of(
                        new OrderLineCreateRequest("skuCode", 1, 1L)
                )
        );
        when(orderService.createOrder(orderCreateRequest)).thenThrow(new NotFoundException("Customer not found"));
        ResponseEntity<Object> responseEntity = orderController.createOrder(orderCreateRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("It should return NOT_FOUND when [create order] and product not found")
    void itShouldReturnNotFoundWhenCreateOrderAndProductNotFound() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                1L,
                1234L,
                LocalDate.of(2021, 1, 1),
                List.of(
                        new OrderLineCreateRequest("skuCode", 1, 1L)
                )
        );
        when(orderService.createOrder(orderCreateRequest)).thenThrow(new NotFoundException("Product not found"));
        ResponseEntity<Object> responseEntity = orderController.createOrder(orderCreateRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("It should return BAD_REQUEST when [create order] and product quantity less than 1")
    void itShouldReturnBadRequestWhenCreateOrderAndProductQuantityLessThan1() {
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                1L,
                1234L,
                LocalDate.of(2021, 1, 1),
                List.of(
                        new OrderLineCreateRequest("skuCode", 0, 1L)
                )
        );
        when(orderService.createOrder(orderCreateRequest)).thenThrow(new IllegalArgumentException("Product quantity less than 1"));
        ResponseEntity<Object> responseEntity = orderController.createOrder(orderCreateRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("It should return OK when [delete order]")
    void itShouldReturnOkWhenDeleteOrder() {
        Long id = 1L;

        ResponseEntity<Object> responseEntity = orderController.deleteOrder(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("It should return NOT_FOUND when [delete order] and order not found")
    void itShouldReturnNotFoundWhenDeleteOrderAndOrderNotFound() {
        Long id = 1L;

        doThrow(new NotFoundException("Order not found")).when(orderService).deleteOrderById(id);

        ResponseEntity<Object> responseEntity = orderController.deleteOrder(id);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("It should return OK when [find order by id]")
    void itShouldReturnOkWhenFindOrderById() {
        Long id = 1L;
        Order order = Order.builder()
                .id(id)
                .customer(Customer.builder()
                        .registrationCode(1L)
                        .email("email")
                        .fullName("customer")
                        .telephone("telephone")
                        .build())
                .dateOfSubmission(LocalDate.of(2021, 1, 1))
                .orderLines(List.of(
                        OrderLine.builder()
                                .id(1L)
                                .product(Product.builder()
                                        .skuCode("skuCode")
                                        .name("name")
                                        .unitPrice(1F)
                                        .build())
                                .quantity(1)
                                .build()
                ))
                .build();
        when(orderService.findOrderById(id)).thenReturn(Optional.of(order));

        ResponseEntity<OrderDto> responseEntity = orderController.findOrderById(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(order.toDto(), responseEntity.getBody());
    }

    @Test
    @DisplayName("It should return NOT_FOUND when [find order by id] and order not found")
    void itShouldReturnNotFoundWhenFindOrderByIdAndOrderNotFound() {
        Long id = 1L;
        when(orderService.findOrderById(id)).thenReturn(Optional.empty());

        ResponseEntity<OrderDto> responseEntity = orderController.findOrderById(id);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("It should return OK when [find orders by date]")
    void itShouldReturnOkWhenFindOrdersByDate() {
        LocalDate dateOfSubmission = LocalDate.of(2021, 1, 1);
        List<Order> orders = List.of(
                Order.builder()
                        .id(1L)
                        .customer(Customer.builder()
                                .registrationCode(1L)
                                .email("email")
                                .fullName("customer")
                                .telephone("telephone")
                                .build())
                        .dateOfSubmission(dateOfSubmission)
                        .orderLines(List.of(
                                OrderLine.builder()
                                        .id(1L)
                                        .product(Product.builder()
                                                .skuCode("skuCode")
                                                .name("name")
                                                .unitPrice(1F)
                                                .build())
                                        .quantity(1)
                                        .build()
                        ))
                        .build()
        );
        when(orderService.findOrdersByDate(dateOfSubmission)).thenReturn(orders);

        ResponseEntity<List<OrderDto>> responseEntity = orderController.findOrdersByDate(dateOfSubmission);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(orders.stream().map(Order::toDto).toList(), responseEntity.getBody());
    }
}