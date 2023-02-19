package com.ordermanagement.orderline;

import com.ordermanagement.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderLineControllerTest {
    @Mock
    private OrderLineService orderLineService;
    @InjectMocks
    private OrderLineController orderLineController;

    @Test
    @DisplayName("It should return OK when [update order line]")
    void itShouldReturnOkWhenUpdateOrderLine() {
        Long id = 1L;
        Integer quantity = 1;

        ResponseEntity<Object> responseEntity = orderLineController.updateQuantity(id, quantity);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(orderLineService, times(1)).updateQuantity(id, quantity);
    }

    @Test
    @DisplayName("It should return BAD_REQUEST when [update order line] and quantity is less than 1")
    public void itShouldReturnBadRequestWhenUpdateOrderLineAndQuantityIsLessThan1() {
        Long id = 1L;
        Integer quantity = 0;

        doThrow(IllegalArgumentException.class).when(orderLineService).updateQuantity(id, quantity);

        ResponseEntity<Object> responseEntity = orderLineController.updateQuantity(id, quantity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("It should return NOT_FOUND when [update order line] and order line not found")
    public void itShouldReturnNotFoundWhenUpdateOrderLineAndOrderLineNotFound() {
        Long id = 1L;
        Integer quantity = 1;

        doThrow(NotFoundException.class).when(orderLineService).updateQuantity(id, quantity);

        ResponseEntity<Object> responseEntity = orderLineController.updateQuantity(id, quantity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }
}