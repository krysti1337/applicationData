package com.example.reports.applicationdata.controller;

import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.service.CustomerServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class CustomerController {

    private final CustomerServiceImpl customerService;

    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customer")
    public List<Customer> getCustomers() {
        return customerService.findAll();
    }

    @GetMapping("/customer/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.findById(id);
    }

    @PostMapping("/customer/save")
    public void saveCustomer(@RequestBody Customer customer) {
        customerService.save(customer);
    }

    @PutMapping("/customer/update/{id}")
    public void updateCustomer(@PathVariable Long id, @RequestBody Customer customer) {
        customer.setCustomerId(id);
        customerService.update(customer);
    }

    @DeleteMapping("/customer/delete/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        Customer customer = customerService.findById(id);
        if (customer != null) {
            customerService.delete(customer);
        }
    }
}
