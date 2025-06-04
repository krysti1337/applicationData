package com.example.reports.applicationdata.ui;

import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.service.CustomerService;
import com.example.reports.applicationdata.service.CustomerServiceImpl;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@Route("/customers")
@PermitAll
public class CustomerView extends VerticalLayout {

    private final CustomerServiceImpl customerService;
    private final Grid<Customer> grid = new Grid<>(Customer.class, false);
    private final TextField countryFilter = new TextField("Search by Country");
    private final TextField idFilter = new TextField("Search by ID");

    public CustomerView(CustomerServiceImpl customerService) {
        this.customerService = customerService;

        setSizeFull();
        add(new H2("Customer Management"));
        configureSearch();
        configureGrid();
        updateGrid();
    }

    private void configureSearch() {
        Button search = new Button("Search", e -> updateGrid());
        Button reset = new Button("Reset", e -> {
            countryFilter.clear();
            idFilter.clear();
            updateGrid();
        });

        HorizontalLayout searchBar = new HorizontalLayout(countryFilter, idFilter, search, reset);
        searchBar.setAlignItems(Alignment.END);
        add(searchBar);
    }

    private void configureGrid() {
        grid.addColumn(Customer::getCustomerId).setHeader("Customer ID");
        grid.addColumn(Customer::getCountry).setHeader("Country");
        grid.addColumn(c -> c.getProfile() != null ? c.getProfile().getName() : "-").setHeader("Profile Name");
        grid.addColumn(c -> c.getTransactions() != null ? c.getTransactions().size() : 0).setHeader("Transactions");

        grid.addComponentColumn(customer -> {
            Button edit = new Button("Edit", e -> openEditDialog(customer));
            Button delete = new Button("Delete", e -> {
                customerService.delete(customer.getCustomerId());
                updateGrid();
            });
            return new HorizontalLayout(edit, delete);
        }).setHeader("Actions");

        grid.setWidthFull();
        add(grid);
    }

    private void openEditDialog(Customer customer) {
        Dialog dialog = new Dialog();
        TextField country = new TextField("Country", customer.getCountry());
        TextField name = new TextField("Profile Name", customer.getProfile() != null ? customer.getProfile().getName() : "");

        Button save = new Button("Save", e -> {
            customer.setCountry(country.getValue());
            if (customer.getProfile() != null) {
                customer.getProfile().setName(name.getValue());
            }
            customerService.update(customer);
            dialog.close();
            updateGrid();
        });

        dialog.add(new VerticalLayout(country, name, save));
        dialog.open();
    }

    private void updateGrid() {
        String country = countryFilter.getValue();
        String id = idFilter.getValue();
        List<Customer> customers = customerService.findByFilter(country, id);
        grid.setItems(customers);
    }
}

