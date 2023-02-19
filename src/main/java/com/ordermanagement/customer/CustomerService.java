package com.ordermanagement.customer;

import com.ordermanagement.exception.IdAlreadyInUseException;
import com.ordermanagement.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRespository customerRespository;

    @Autowired
    public CustomerService(CustomerRespository customerRespository) {
        this.customerRespository = customerRespository;
    }

    public Customer createCustomer(Customer customer) {
        if(findCustomerByCode(customer.getRegistrationCode()).isPresent())
            throw new IdAlreadyInUseException("Customer with id [%d] already exists".formatted(customer.getRegistrationCode()));
        return customerRespository.save(customer);
    }

    public Optional<Customer> findCustomerByCode(Long code) {
        return customerRespository.findById(code);
    }

    public Customer updateCustomer(Customer customer) {
        if(findCustomerByCode(customer.getRegistrationCode()).isEmpty())
            throw new NotFoundException("Customer with id [%d] not found".formatted(customer.getRegistrationCode()));
        return customerRespository.save(customer);
    }

    public void deleteCustomerByCode(Long code) {
        if(findCustomerByCode(code).isEmpty())
            throw new NotFoundException("Customer with id [%d] not found".formatted(code));
        customerRespository.deleteById(code);
    }
}
