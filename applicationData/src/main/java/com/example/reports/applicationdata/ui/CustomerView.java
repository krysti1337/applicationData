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
    private final Grid<Customer> grid = new Grid<>(Customer.class, false); // explicit fără reflecție automată
    private final TextField searchField = new TextField("Search by Country");
    private final TextField idField = new TextField("Customer ID");
    private final TextField countryField = new TextField("Country");

    @Autowired
    public CustomerView(GenericService<Customer, Long> customerService) {
        this.customerService = customerService;

        Button searchButton = new Button("Search", e -> searchCustomers());
        Button resetButton = new Button("Reset", e -> loadAllCustomers());
        Button saveButton = new Button("Save", e -> saveCustomer());

        FormLayout form = new FormLayout();
        form.add(idField, countryField, saveButton);

        configureGrid();

        add(searchField, searchButton, resetButton, form, grid);
        loadAllCustomers();
    }

    private void configureGrid() {
        grid.addColumn(Customer::getCustomerId).setHeader("Customer Id").setAutoWidth(true);
        grid.addColumn(Customer::getCountry).setHeader("Country").setAutoWidth(true);
        grid.addColumn(customer -> {
            if (customer.getTransactions() == null || customer.getTransactions().isEmpty()) {
                return "-";
            }
            return customer.getTransactions().stream()
                    .map(t -> String.valueOf(t.getId()))
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("-");
        }).setHeader("Transaction Ids").setAutoWidth(true);
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

        // Adauga in viitor profile
        Profile profile = new Profile();
        customer.setProfile(null);

        customerService.save(customer);
        loadAllCustomers();
    }
}

