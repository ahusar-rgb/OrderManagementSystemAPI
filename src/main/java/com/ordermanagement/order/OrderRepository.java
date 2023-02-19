package com.ordermanagement.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository("orderJpaRepository")
public interface OrderRepository extends JpaRepository<Order, Long>, OrderFinder {

    @Query("SELECT o FROM Order o WHERE o.dateOfSubmission = :date")
    List<Order> findOrdersByDate(LocalDate date);

    @Query("SELECT o FROM Order o WHERE :skuCode IN (SELECT ol.product.skuCode FROM o.orderLines ol)")
    List<Order> findOrdersByProductSku(String skuCode);

    @Query("SELECT o FROM Order o WHERE o.customer.registrationCode = :customerCode")
    List<Order> findOrdersByCustomerCode(Long customerCode);
}
