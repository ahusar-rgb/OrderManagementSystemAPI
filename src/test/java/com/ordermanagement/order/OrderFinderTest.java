package com.ordermanagement.order;

import com.ordermanagement.customer.Customer;
import com.ordermanagement.customer.CustomerRespository;
import com.ordermanagement.orderline.OrderLine;
import com.ordermanagement.orderline.OrderLineRepository;
import com.ordermanagement.product.Product;
import com.ordermanagement.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class OrderFinderTest {
    private final OrderRepositoryCriteria orderRepositoryCriteria;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CustomerRespository customerRepository;
    private final OrderLineRepository orderLineRepository;

    @Autowired
    public OrderFinderTest(
            OrderRepositoryCriteria orderRepositoryCriteria,
            OrderRepository orderRepository,
            ProductRepository productRepository,
            CustomerRespository customerRepository,
            OrderLineRepository orderLineRepository
    ) {
        this.orderRepositoryCriteria = orderRepositoryCriteria;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderLineRepository = orderLineRepository;
        setup();
    }


    @Test
    @DisplayName("It runs all repository tests")
    void runAllRepositoryTests() {
        runAllTestsForOrderRepository();
        runAllTestsForOrderFinderCriteria();
    }

    private void cleanDb() {
        orderLineRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();
    }

    private void setup() {
        cleanDb();

        Product product1 = Product.builder().skuCode("skuCode1").name("name1").unitPrice(1F).build();
        Product product2 = Product.builder().skuCode("skuCode2").name("name2").unitPrice(2F).build();
        Product product3 = Product.builder().skuCode("skuCode3").name("name3").unitPrice(3F).build();
        productRepository.saveAll(List.of(product1, product2, product3));

        Customer customer1 = Customer.builder().registrationCode(1L).fullName("fullName1").email("email1").telephone("telephone1").build();
        Customer customer2 = Customer.builder().registrationCode(2L).fullName("fullName2").email("email2").telephone("telephone2").build();
        Customer customer3 = Customer.builder().registrationCode(3L).fullName("fullName3").email("email3").telephone("telephone3").build();
        customerRepository.saveAll(List.of(customer1, customer2, customer3));

        Order order1 = Order.builder().id(1L).customer(customer1).dateOfSubmission(LocalDate.of(2021, 01, 01)).build();
        Order order2 = Order.builder().id(2L).customer(customer2).dateOfSubmission(LocalDate.of(2022, 02, 02)).build();
        Order order3 = Order.builder().id(3L).customer(customer3).dateOfSubmission(LocalDate.of(2023, 03, 03)).build();
        orderRepository.saveAll(List.of(order1, order2, order3));

        OrderLine orderLine1 = OrderLine.builder().id(1L).order(order1).product(product1).quantity(1).build();
        OrderLine orderLine2 = OrderLine.builder().id(2L).order(order2).product(product2).quantity(2).build();
        OrderLine orderLine3 = OrderLine.builder().id(3L).order(order3).product(product3).quantity(3).build();
        orderLineRepository.saveAll(List.of(orderLine1, orderLine2, orderLine3));
    }

    private void runAllTestsForOrderFinderCriteria() {
        runAllTestsForOrderFinder(orderRepositoryCriteria);
    }

    void runAllTestsForOrderRepository() {
        itShouldFindOrdersByDate();
        runAllTestsForOrderFinder(orderRepository);
    }


    private void runAllTestsForOrderFinder(OrderFinder orderFinder) {
        findOrdersByProductSku(orderFinder);
        findOrdersByCustomerCode(orderFinder);
    }

    //@Test
    @DisplayName("It should find orders by date")
    void itShouldFindOrdersByDate() {
        List<Order> orders = orderRepository.findOrdersByDate(LocalDate.of(2021, 01, 01));
        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).getId());

        orders = orderRepository.findOrdersByDate(LocalDate.of(2022, 02, 02));
        assertEquals(1, orders.size());
        assertEquals(2L, orders.get(0).getId());

        orders = orderRepository.findOrdersByDate(LocalDate.of(2023, 03, 03));
        assertEquals(1, orders.size());
        assertEquals(3L, orders.get(0).getId());
    }

    //@Test
    //@DisplayName("It should find orders by product sku")
    void findOrdersByProductSku(OrderFinder orderFinder) {
        List<Order> orders = orderFinder.findOrdersByProductSku("skuCode1");
        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).getId());

        orders = orderFinder.findOrdersByProductSku("skuCode2");
        assertEquals(1, orders.size());
        assertEquals(2L, orders.get(0).getId());

        orders = orderFinder.findOrdersByProductSku("skuCode3");
        assertEquals(1, orders.size());
        assertEquals(3L, orders.get(0).getId());
    }

    //@Test
    //@DisplayName("It should find orders by customer code")
    void findOrdersByCustomerCode(OrderFinder orderFinder) {
        List<Order> orders = orderFinder.findOrdersByCustomerCode(1L);
        assertEquals(1, orders.size());
        assertEquals(1L, orders.get(0).getId());

        orders = orderFinder.findOrdersByCustomerCode(2L);
        assertEquals(1, orders.size());
        assertEquals(2L, orders.get(0).getId());

        orders = orderFinder.findOrdersByCustomerCode(3L);
        assertEquals(1, orders.size());
        assertEquals(3L, orders.get(0).getId());
    }
}