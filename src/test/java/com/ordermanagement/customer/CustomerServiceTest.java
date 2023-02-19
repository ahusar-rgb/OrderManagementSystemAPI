package com.ordermanagement.customer;

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
class CustomerServiceTest {

    @Mock
    private CustomerRespository customerRepository;
    @InjectMocks
    private CustomerService customerService;

    @Test
    @DisplayName("It should create customer")
    void itShouldCreateCustomer() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();

        when(customerRepository.save(expected)).thenReturn(expected);

        Customer actual = customerService.createCustomer(expected);

        assertEquals(expected, actual);
        verify(customerRepository, times(1)).save(expected);
    }

    @Test
    @DisplayName("It should not create customer when customer already exists")
    void itShouldNotCreateCustomerWhenCustomerAlreadyExists() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();

        when(customerRepository.findById(expected.getRegistrationCode())).thenReturn(Optional.of(expected));

        assertThrows(IdAlreadyInUseException.class, () -> customerService.createCustomer(expected));
    }

    @Test
    @DisplayName("It should find customer by registration code")
    void itShouldFindCustomerByCode() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();

        when(customerRepository.findById(expected.getRegistrationCode())).thenReturn(Optional.of(expected));

        Optional<Customer> actualOptional = customerService.findCustomerByCode(expected.getRegistrationCode());

        assertTrue(actualOptional.isPresent());

        assertEquals(expected, actualOptional.get());
        verify(customerRepository, times(1)).findById(expected.getRegistrationCode());
    }

    @Test
    @DisplayName("It should find customer optional by registration code")
    void itShouldFindCustomerOptionalByCode() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();

        when(customerRepository.findById(expected.getRegistrationCode())).thenReturn(Optional.empty());

        Optional<Customer> actualOptional = customerService.findCustomerByCode(expected.getRegistrationCode());

        assertTrue(actualOptional.isEmpty());
        verify(customerRepository, times(1)).findById(expected.getRegistrationCode());
    }

    @Test
    @DisplayName("It should update customer")
    void itShouldUpdateCustomer() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();

        when(customerRepository.findById(expected.getRegistrationCode())).thenReturn(Optional.of(expected));
        when(customerRepository.save(expected)).thenReturn(expected);

        Customer actual = customerService.updateCustomer(expected);

        assertEquals(expected, actual);
        verify(customerRepository, times(1)).save(expected);
    }

    @Test
    @DisplayName("It should not update customer when customer does not exist")
    void itShouldNotUpdateCustomerWhenCustomerDoesNotExist() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();

        when(customerRepository.findById(expected.getRegistrationCode())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.updateCustomer(expected));
    }

    @Test
    @DisplayName("It should delete customer by code")
    void itShouldDeleteCustomerByCode() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();

        when(customerRepository.findById(expected.getRegistrationCode())).thenReturn(Optional.of(expected));

        customerService.deleteCustomerByCode(expected.getRegistrationCode());

        verify(customerRepository, times(1)).deleteById(expected.getRegistrationCode());
    }

    @Test
    @DisplayName("It should not delete customer by code when customer does not exist")
    void itShouldNotDeleteCustomerByCodeWhenCustomerDoesNotExist() {
        Customer expected = Customer.builder()
                .registrationCode(1L)
                .email("email")
                .build();

        when(customerRepository.findById(expected.getRegistrationCode())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> customerService.deleteCustomerByCode(expected.getRegistrationCode()));
    }
}