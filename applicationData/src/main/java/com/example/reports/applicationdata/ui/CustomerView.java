package com.example.reports.applicationdata.ui;

import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.model.Profile;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Route("/customers")
public class CustomerView extends VerticalLayout {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Grid<Customer> grid = new Grid<>(Customer.class, false); // explicit fără reflecție automată
    private final TextField searchField = new TextField("Search by Country");
    private final TextField idField = new TextField("Customer ID");
    private final TextField countryField = new TextField("Country");

    public CustomerView() {

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
        Customer[] customers = restTemplate.getForObject("http://localhost:8081/api/customer", Customer[].class);
        grid.setItems(Arrays.asList(customers));
    }

    private void searchCustomers() {
        String keyword = searchField.getValue().toLowerCase();
        Customer[] customers = restTemplate.getForObject("http://localhost:8081/api/customer", Customer[].class);
        List<Customer> filtered = Arrays.stream(customers)
                .filter(c -> c.getCountry() != null && c.getCountry().toLowerCase().contains(keyword))
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

        restTemplate.postForObject("http://localhost:8081/api/customer/save", customer, Void.class);
        loadAllCustomers();
    }
}
