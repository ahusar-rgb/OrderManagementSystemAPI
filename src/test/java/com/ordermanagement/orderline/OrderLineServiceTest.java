package com.ordermanagement.orderline;

import com.ordermanagement.exception.IdAlreadyInUseException;
import com.ordermanagement.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderLineServiceTest {
    @Mock
    private OrderLineRepository orderLineRepository;

    @InjectMocks
    private OrderLineService orderLineService;

    @Test
    @DisplayName("It should create order line")
    void itShouldCreateOrderLine() {
        OrderLine expected = OrderLine.builder()
                .id(1L)
                .quantity(1)
                .build();
        when(orderLineRepository.save(expected)).thenReturn(expected);

        OrderLine actual = orderLineService.createOrderLine(expected);

        assertEquals(expected, actual);
        verify(orderLineRepository, times(1)).save(expected);
    }

    @Test
    @DisplayName("It should not create order line when quantity is less than 1")
    void itShouldNotCreateOrderLineWhenQuantityIsLessThan1() {
        OrderLine expected = OrderLine.builder()
                .id(1L)
                .quantity(0)
                .build();

        assertThrows(IllegalArgumentException.class, () -> orderLineService.createOrderLine(expected));
        verify(orderLineRepository, never()).save(any());
    }

    @Test
    @DisplayName("It should not create order line when id already in use")
    void itShouldNotCreateOrderLineWhenIdAlreadyInUse() {
        OrderLine expected = OrderLine.builder()
                .id(1L)
                .quantity(1)
                .build();
        when(orderLineRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        assertThrows(IdAlreadyInUseException.class, () -> orderLineService.createOrderLine(expected));
        verify(orderLineRepository, never()).save(any());
    }

    @Test
    @DisplayName("It should find order line by id")
    void itShouldFindOrderLineById() {
        OrderLine expected = OrderLine.builder()
                .id(1L)
                .quantity(1)
                .build();
        when(orderLineRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Optional<OrderLine> actual = orderLineService.findOrderLineById(expected.getId());
        assertTrue(actual.isPresent());
        assertEquals(expected, actual.get());
    }

    @Test
    @DisplayName("It should find order line optional when order line is not found")
    void itShouldFindOrderOptionalLineById() {
        OrderLine expected = OrderLine.builder()
                .id(1L)
                .quantity(1)
                .build();
        when(orderLineRepository.findById(expected.getId())).thenReturn(Optional.empty());

        Optional<OrderLine> actual = orderLineService.findOrderLineById(expected.getId());
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("It should delete order line")
    void itShouldDeleteOrderLine() {
        OrderLine expected = OrderLine.builder()
                .id(1L)
                .quantity(1)
                .build();
        when(orderLineRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        orderLineService.deleteOrderLine(expected.getId());
        verify(orderLineRepository, times(1)).deleteById(expected.getId());
    }

    @Test
    @DisplayName("It should not delete order line when order line is not found")
    void itShouldNotDeleteOrderLineWhenOrderLineIsNotFound() {
        OrderLine expected = OrderLine.builder()
                .id(1L)
                .quantity(1)
                .build();
        when(orderLineRepository.findById(expected.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderLineService.deleteOrderLine(expected.getId()));
        verify(orderLineRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("It should update order line quantity")
    void itShouldUpdateOrderLineQuantity() {
        OrderLine expected = OrderLine.builder()
                .id(1L)
                .quantity(1)
                .build();
        when(orderLineRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        orderLineService.updateQuantity(expected.getId(), 2);
        verify(orderLineRepository, times(1)).save(expected);
    }

    @Test
    @DisplayName("It should not update order line quantity when order line is not found")
    void itShouldNotUpdateOrderLineQuantityWhenOrderLineIsNotFound() {
        OrderLine expected = OrderLine.builder()
                .id(1L)
                .quantity(1)
                .build();
        when(orderLineRepository.findById(expected.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> orderLineService.updateQuantity(expected.getId(), 2));
        verify(orderLineRepository, never()).save(any());
    }

    @Test
    @DisplayName("It should not update order line quantity when quantity is less than 1")
    void itShouldNotUpdateOrderLineQuantityWhenQuantityIsLessThan1() {
        OrderLine expected = OrderLine.builder()
                .id(1L)
                .quantity(1)
                .build();
        when(orderLineRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        assertThrows(IllegalArgumentException.class, () -> orderLineService.updateQuantity(expected.getId(), 0));
        verify(orderLineRepository, never()).save(any());
    }
}