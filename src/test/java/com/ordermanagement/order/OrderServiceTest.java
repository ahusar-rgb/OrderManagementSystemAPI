package com.ordermanagement.order;

import com.ordermanagement.customer.Customer;
import com.ordermanagement.customer.CustomerService;
import com.ordermanagement.exception.NotFoundException;
import com.ordermanagement.orderline.OrderLineCreateRequest;
import com.ordermanagement.orderline.OrderLineService;
import com.ordermanagement.product.Product;
import com.ordermanagement.product.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderFinder orderFinder;

    @Mock
    private CustomerService customerService;
    @Mock
    private OrderLineService orderLineService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("It should create order")
    void itShouldCreateOrder() {
        List<OrderLineCreateRequest> orderLines = List.of(
                new OrderLineCreateRequest("skuCode", 1, 1L)
        );
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                1L,
                1234L,
                LocalDate.of(2021, 1, 1),
                orderLines
        );

        when(customerService.findCustomerByCode(orderCreateRequest.customerCode())).thenReturn(
                Optional.of(Customer.builder()
                        .registrationCode(orderCreateRequest.customerCode())
                        .fullName("name")
                        .build())
        );

        when(productService.findProductBySkuCode(orderCreateRequest.orderLines().get(0).productSkuCode())).thenReturn(
                Optional.of(new Product(
                        orderCreateRequest.orderLines().get(0).productSkuCode(),
                        "name",
                        1.0f))
        );
        when(orderRepository.save(any())).thenReturn(Order.builder().build());

        orderService.createOrder(orderCreateRequest);

        verify(orderRepository, times(1)).save(any());
        verify(orderLineService, times(orderLines.size())).createOrderLine(any());
    }

    @Test
    @DisplayName("It should not create order when customer not found")
    public void itShouldNotCreateOrderWhenCustomerNotFound() {
        List<OrderLineCreateRequest> orderLines = List.of(
                new OrderLineCreateRequest("skuCode", 1, 1L)
        );
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                1L,
                1234L,
                LocalDate.of(2021, 1, 1),
                orderLines
        );

        when(customerService.findCustomerByCode(orderCreateRequest.customerCode())).thenReturn(
                Optional.empty()
        );

        assertThrows(NotFoundException.class, () -> orderService.createOrder(orderCreateRequest));
    }

    @Test
    @DisplayName("It should not create order when product not found")
    public void itShouldNotCreateOrderWhenProductNotFound() {
        List<OrderLineCreateRequest> orderLines = List.of(
                new OrderLineCreateRequest("skuCode", 1, 1L)
        );
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                1L,
                1234L,
                LocalDate.of(2021, 1, 1),
                orderLines
        );

        when(customerService.findCustomerByCode(orderCreateRequest.customerCode())).thenReturn(
                Optional.of(Customer.builder()
                        .registrationCode(orderCreateRequest.customerCode())
                        .fullName("name")
                        .build())
        );

        when(productService.findProductBySkuCode(orderCreateRequest.orderLines().get(0).productSkuCode())).thenReturn(
                Optional.empty()
        );

        assertThrows(NotFoundException.class, () -> orderService.createOrder(orderCreateRequest));
    }

    @Test
    @DisplayName("It should find order by id")
    void itShouldFindOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(Order.builder().build()));

        orderService.findOrderById(1L);

        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("It should return empty order optional when f[indById] and order not found")
    void itShouldReturnEmptyOrderOptionalWhenFindByIdAndOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Order> order = orderService.findOrderById(1L);

        verify(orderRepository, times(1)).findById(1L);
        assertTrue(order.isEmpty());
    }

    @Test
    @DisplayName("It should update order")
    void itShouldUpdateOrder() {
        List<OrderLineCreateRequest> orderLines = List.of(
                new OrderLineCreateRequest("skuCode", 1, 1L)
        );
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                1L,
                1234L,
                LocalDate.of(2021, 1, 1),
                orderLines
        );

        when(customerService.findCustomerByCode(orderCreateRequest.customerCode())).thenReturn(
                Optional.of(Customer.builder()
                        .registrationCode(orderCreateRequest.customerCode())
                        .fullName("name")
                        .build())
        );

        when(productService.findProductBySkuCode(orderCreateRequest.orderLines().get(0).productSkuCode())).thenReturn(
                Optional.of(new Product(
                        orderCreateRequest.orderLines().get(0).productSkuCode(),
                        "name",
                        1.0f))
        );
        when(orderRepository.findById(1L)).thenReturn(Optional.of(Order.builder().build()));
        when(orderRepository.save(any())).thenReturn(Order.builder().build());

        orderService.updateOrder(orderCreateRequest);

        verify(orderRepository, times(1)).save(any());
        verify(orderLineService, times(orderLines.size())).createOrderLine(any());
    }

    @Test
    @DisplayName("It should not update order when it is not found")
    void itShouldNotUpdateOrderWhenItIsNotFound() {
        List<OrderLineCreateRequest> orderLines = List.of(
                new OrderLineCreateRequest("skuCode", 1, 1L)
        );
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                1L,
                1234L,
                LocalDate.of(2021, 1, 1),
                orderLines
        );

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.updateOrder(orderCreateRequest));
    }

    @Test
    @DisplayName("It should delete order by id")
    void itShouldDeleteOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(Order.builder().build()));

        orderService.deleteOrderById(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("It should not delete order when it is not found")
    void itShouldNotDeleteOrderWhenItIsNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderService.deleteOrderById(1L));
    }

    @Test
    @DisplayName("It should call [findOrdersByDate] from repository")
    void itShouldCallFindOrdersByDateFromRepository() {
        when(orderRepository.findOrdersByDate(any())).thenReturn(List.of(Order.builder().build()));

        LocalDate date = LocalDate.of(2021, 1, 1);

        orderService.findOrdersByDate(date);

        verify(orderRepository, times(1)).findOrdersByDate(date);
    }

    //@Test
    @DisplayName("It should call [findOrdersByProductSku] from repository")
    void itShouldCallFindOrdersByProductSkuFromRepository() {
        when(orderFinder.findOrdersByProductSku(any())).thenReturn(List.of(Order.builder().build()));

        String skuCode = "skuCode";
        orderService.findOrdersByProductSku(skuCode);

        verify(orderFinder, times(1)).findOrdersByProductSku(skuCode);
    }

    //@Test
    @DisplayName("It should call [findOrdersByCustomer] from repository")
    void itShouldCallFindOrdersByCustomerCodeFromRepository() {
        orderService.findOrdersByCustomer(1L);

        verify(orderFinder, times(1)).findOrdersByCustomerCode(1L);
    }
}
