package com.ordermanagement.order;

import java.util.List;

public interface OrderFinder {
    List<Order> findOrdersByProductSku(String skuCode);
    List<Order> findOrdersByCustomerCode(Long registrationCode);
}
