package com.ordermanagement.orderline;

import com.ordermanagement.exception.IdAlreadyInUseException;
import com.ordermanagement.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderLineService {
    private final OrderLineRepository orderLineRepository;
    @Autowired
    public OrderLineService(OrderLineRepository orderLineRepository) {
        this.orderLineRepository = orderLineRepository;
    }
    public OrderLine createOrderLine(OrderLine orderLine) {
        if (orderLineRepository.findById(orderLine.getId()).isPresent())
            throw new IdAlreadyInUseException("Order line [%d] already exists".formatted(orderLine.getId()));

        if(orderLine.getQuantity() < 1)
            throw new IllegalArgumentException("Quantity must be greater than 0");

        return orderLineRepository.save(orderLine);
    }
    public Optional<OrderLine> findOrderLineById(Long id) {
        return orderLineRepository.findById(id);
    }
    public void deleteOrderLine(Long id) {
        if (orderLineRepository.findById(id).isEmpty())
            throw new NotFoundException("Order line [%d] not found".formatted(id));

        orderLineRepository.deleteById(id);
    }

    public void updateQuantity(Long id, Integer quantity) {
        OrderLine orderLine = orderLineRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Order line [%d] not found".formatted(id))
        );

        if(quantity < 1)
            throw new IllegalArgumentException("Quantity must be greater than 0");

        orderLine.setQuantity(quantity);
        orderLineRepository.save(orderLine);
    }
}
