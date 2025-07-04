package com.example.reports.applicationdata.service;

import com.example.reports.applicationdata.dao.impl.GenericDao;
import com.example.reports.applicationdata.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private GenericDao<Customer, Long> customerDao;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    public void testSaveCustomer(){

        Customer customer = new Customer();
        customer.setCustomerId(1L);
        customer.setCountry("Italy");

        customerService.save(customer);

        verify(customerDao).save(customer);
    }

    @Test
    public void testFindByCustomerId(){
        Customer customer = new Customer();
        customer.setCustomerId(2L);
        customer.setCountry("France");

        when(customerDao.findById(2L)).thenReturn(customer);

        Customer result  = customerService.findById(2L);

        assertNotNull(result);
        assertEquals("France", result.getCountry());

        verify(customerDao).findById(2L);
    }

    @Test
    public void testFindAllCustomers(){
        List<Customer> customers = List.of(
                new Customer() {{
                    setCustomerId(1L);
                    setCountry("Italy");
                }},
                new Customer() {{
                    setCustomerId(2L);
                    setCountry("France");
                }},
                new Customer() {{
                    setCustomerId(3L);
                    setCountry("Spain");
                }}
        );

        when(customerDao.findAll()).thenReturn(customers);

        List<Customer> result  = customerService.findAll();

        assertEquals(3, result.size());
        verify(customerDao).findAll();
    }

    @Test
    public void testUpdateCustomer(){
        Customer customer = new Customer();
        customer.setCustomerId(3L);
        customer.setCountry("Spain");
        customerService.save(customer);

        customer.setCountry("Italy");
        customerService.update(customer);

        verify(customerDao).update(customer);
    }

    @Test
    public void testDelete(){
        Customer customer = new Customer();
        customer.setCustomerId(4L);
        customer.setCountry("Australia");

        customerService.delete(customer);

        verify(customerDao).delete(customer);
    }

}
