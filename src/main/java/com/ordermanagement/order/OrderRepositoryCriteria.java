package com.ordermanagement.order;

import com.ordermanagement.orderline.OrderLine;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("orderCriteriaRepository")
@RequiredArgsConstructor
public class OrderRepositoryCriteria implements OrderFinder{
    private final EntityManager entityManager;
    public List<Order> findOrdersByProductSku(String skuCode) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);

        Root<Order> root = criteriaQuery.from(Order.class);
        Join<Order, OrderLine> orderLineJoin = root.join("orderLines");
        Predicate predicate = criteriaBuilder.equal(orderLineJoin.get("product").get("skuCode"), skuCode);
        criteriaQuery.where(predicate);

        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }

    public List<Order> findOrdersByCustomerCode(Long registrationCode) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);

        Root<Order> root = criteriaQuery.from(Order.class);
        Predicate predicate = criteriaBuilder.equal(root.get("customer").get("registrationCode"), registrationCode);
        criteriaQuery.where(predicate);

        TypedQuery<Order> query = entityManager.createQuery(criteriaQuery);
        return query.getResultList();
    }
}
