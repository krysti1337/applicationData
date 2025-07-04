package com.example.reports.applicationdata.ui;

import com.example.reports.applicationdata.model.Product;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Route("/products")
public class ProductView extends VerticalLayout {

    private final RestTemplate restTemplate = new RestTemplate();
    private final Grid<Product> grid = new Grid<>(Product.class, false);

    private final TextField stockCodeField = new TextField("Stock Code");
    private final TextField descriptionField = new TextField("Description");
    private final NumberField unitPriceField = new NumberField("Unit Price");

    public ProductView() {

        Button saveButton = new Button("Save", e -> saveProduct());
        FormLayout form = new FormLayout(stockCodeField, descriptionField, unitPriceField, saveButton);

        configureGrid();

        add(form, grid);
        loadAllProducts();
    }

    private void configureGrid() {
        grid.addColumn(Product::getStockCode).setHeader("Stock Code").setAutoWidth(true);
        grid.addColumn(Product::getDescription).setHeader("Description").setAutoWidth(true);
        grid.addColumn(product -> product.getUnitPrice() != null ? product.getUnitPrice().toString() : "-")
                .setHeader("Unit Price").setAutoWidth(true);
    }

    private void loadAllProducts() {
        Product[] products = restTemplate.getForObject("http://localhost:8081/api/product", Product[].class);
        grid.setItems(Arrays.asList(products));
    }

    private void saveProduct() {
        Product product = new Product();
        product.setStockCode(stockCodeField.getValue());
        product.setDescription(descriptionField.getValue());
        product.setUnitPrice(BigDecimal.valueOf(unitPriceField.getValue() != null ? unitPriceField.getValue() : 0.0));

        restTemplate.postForObject("http://localhost:8081/api/product/save", product, Void.class);
        loadAllProducts();
    }
}