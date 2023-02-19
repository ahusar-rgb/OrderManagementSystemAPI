package com.ordermanagement.customer;

import com.ordermanagement.exception.IdAlreadyInUseException;
import com.ordermanagement.exception.NotFoundException;
import com.ordermanagement.order.OrderDto;
import com.ordermanagement.order.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
    @Mock
    private CustomerService customerService;
    @Mock
    private OrderService orderService;
    @InjectMocks
    private CustomerController customerController;

    @Test
    @DisplayName("It should return OK when [create customer]")
    void itShouldReturnOkWhenCreateCustomer() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();
        when(customerService.createCustomer(expected)).thenReturn(expected);

        ResponseEntity<Customer> responseEntity = customerController.createCustomer(expected);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expected, responseEntity.getBody());
        verify(customerService, times(1)).createCustomer(expected);
    }

    @Test
    @DisplayName("It should return BAD_REQUEST when [create customer] and customer already exists")
    void itShouldReturnBadRequestWhenCreateCustomerAndCustomerAlreadyExists() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();
        when(customerService.createCustomer(expected)).thenThrow(IdAlreadyInUseException.class);

        ResponseEntity<Customer> responseEntity = customerController.createCustomer(expected);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("It should return OK when [delete customer by code]")
    void itShouldReturnOkWhenDeleteCustomerByCode() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();

        ResponseEntity<Object> responseEntity = customerController.deleteCustomerByCode(expected.getRegistrationCode());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(customerService, times(1)).deleteCustomerByCode(expected.getRegistrationCode());
    }

    @Test
    @DisplayName("It should return NOT_FOUND when [delete customer by code] and customer not found")
    void itShouldReturnNotFoundWhenDeleteCustomerByIdAndCustomerNotFound() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();

        doThrow(NotFoundException.class).when(customerService).deleteCustomerByCode(expected.getRegistrationCode());
        ResponseEntity<Object> responseEntity = customerController.deleteCustomerByCode(expected.getRegistrationCode());

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(customerService, times(1)).deleteCustomerByCode(expected.getRegistrationCode());
    }

    @Test
    @DisplayName("It should return OK when [find customer by code]")
    void itShouldReturnOkWhenFindCustomerByCode() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();
        when(customerService.findCustomerByCode(expected.getRegistrationCode())).thenReturn(Optional.of(expected));

        ResponseEntity<Customer> responseEntity = customerController.findCustomerByCode(expected.getRegistrationCode());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expected, responseEntity.getBody());
    }

    @Test
    @DisplayName("It should return NOT_FOUND when [find customer by code] and customer not found")
    void itShouldReturnNotFoundWhenFindCustomerByCodeAndCustomerNotFound() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();
        when(customerService.findCustomerByCode(expected.getRegistrationCode())).thenReturn(Optional.empty());

        ResponseEntity<Customer> responseEntity = customerController.findCustomerByCode(expected.getRegistrationCode());

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    @DisplayName("It should call [find by customer code] from orderService")
    void itShouldCallFindByCustomerCodeFromOrderService() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();

        ResponseEntity<List<OrderDto>> responseEntity = customerController.findOrdersByCustomerCode(expected.getRegistrationCode());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(orderService, times(1)).findOrdersByCustomer(expected.getRegistrationCode());
    }
}