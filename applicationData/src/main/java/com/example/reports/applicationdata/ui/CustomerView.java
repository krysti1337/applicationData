package com.example.reports.applicationdata.ui;

import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.model.Profile;
import com.example.reports.applicationdata.service.GenericService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Route("/customers")
@Component
public class CustomerView extends VerticalLayout {

    private final GenericService<Customer, Long> customerService;
    private final Grid<Customer> grid = new Grid<>(Customer.class);
    private final TextField searchField = new TextField("Search by Country");
    private final TextField idField = new TextField("Customer ID");
    private final TextField countryField = new TextField("Country");
    private final TextField emailField = new TextField("Email");
    private final TextField addressField = new TextField("Address");
    private final TextField phoneField = new TextField("Phone");

    @Autowired
    public CustomerView(GenericService<Customer, Long> customerService) {
        this.customerService = customerService;

        Button searchButton = new Button("Search", e -> searchCustomers());
        Button resetButton = new Button("Reset", e -> loadAllCustomers());
        Button saveButton = new Button("Save", e -> saveCustomer());

        FormLayout form = new FormLayout();
        form.add(idField, countryField, emailField, addressField, phoneField, saveButton);

        add(searchField, searchButton, resetButton, form, grid);
        loadAllCustomers();
    }

    private void loadAllCustomers() {
        List<Customer> customers = customerService.findAll();
        grid.setItems(customers);
    }

    private void searchCustomers() {
        String keyword = searchField.getValue();
        List<Customer> filtered = customerService.findAll().stream()
                .filter(c -> c.getCountry() != null && c.getCountry().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
        grid.setItems(filtered);
    }

    private void saveCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(Long.parseLong(idField.getValue()));
        customer.setCountry(countryField.getValue());

        Profile profile = new Profile();
        profile.setEmail(emailField.getValue());
        profile.setAddress(addressField.getValue());
        profile.setPhone(phoneField.getValue());

        customer.setProfile(profile);
        customerService.save(customer);
        loadAllCustomers();
    }
}

