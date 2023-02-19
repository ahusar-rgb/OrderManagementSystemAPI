package com.ordermanagement.order;

import com.ordermanagement.customer.Customer;
import com.ordermanagement.customer.CustomerService;
import com.ordermanagement.exception.NotFoundException;
import com.ordermanagement.orderline.OrderLine;
import com.ordermanagement.orderline.OrderLineService;
import com.ordermanagement.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderFinder orderFinder;
    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderLineService orderLineService;
    @Autowired
    public OrderService(OrderRepository orderRepository, @Qualifier("orderCriteriaRepository") OrderFinder orderFinder, CustomerService customerService, ProductService productService, OrderLineService orderLineService) {
        this.orderRepository = orderRepository;
        this.orderFinder = orderFinder;
        this.customerService = customerService;
        this.productService = productService;
        this.orderLineService = orderLineService;
    }

    public Order createOrder(OrderCreateRequest orderCreateRequest) {
        Customer customer = customerService.findCustomerByCode(orderCreateRequest.customerCode()).orElseThrow(
                () -> new NotFoundException("Customer not found")
        );

        Order orderData = Order.builder()
                .customer(customer)
                .dateOfSubmission(orderCreateRequest.dateOfSubmission())
                .orderLines(new ArrayList<>())
                .build();

        Order order = orderRepository.save(orderData);

        List<OrderLine> orderLines = orderCreateRequest.orderLines().stream().map(
                orderLineCreateRequest ->
                        OrderLine.builder()
                                .order(order)
                                .product(productService.findProductBySkuCode(orderLineCreateRequest.productSkuCode()).orElseThrow(
                                        () -> new NotFoundException("Product not found")
                                ))
                                .quantity(orderLineCreateRequest.quantity())
                                .build()
        ).toList();

        orderLines.forEach(orderLineService::createOrderLine);

        order.setOrderLines(orderLines);
        return order;
    }

    public Optional<Order> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public Order updateOrder(OrderCreateRequest orderCreateRequest) {
        if(findOrderById(orderCreateRequest.id()).isEmpty())
            throw new NotFoundException("Order not found");
        return createOrder(orderCreateRequest);
    }

    public void deleteOrderById(Long id) {
        if(findOrderById(id).isEmpty())
            throw new NotFoundException("Order not found");
        orderRepository.deleteById(id);
    }

    public List<Order> findOrdersByDate(LocalDate date) {
        return orderRepository.findOrdersByDate(date);
    }

    public List<Order> findOrdersByProductSku(String skuCode) {
        return orderFinder.findOrdersByProductSku(skuCode);
    }

    public List<Order> findOrdersByCustomer(Long customerId) {
        return orderFinder.findOrdersByCustomerCode(customerId);
    }
}
