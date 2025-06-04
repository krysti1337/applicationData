package com.example.reports.applicationdata.ui;

import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.model.Profile;
import com.example.reports.applicationdata.model.Product;
//import com.example.reports.applicationdata.model.Category;
import com.example.reports.applicationdata.service.GenericService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Route("/products")
@Component
public class ProductView extends VerticalLayout {

    private final GenericService<Product, String> productService;
//    private final GenericService<Category, Long> categoryService;
    private final Grid<Product> grid = new Grid<>(Product.class);
    private final TextField stockCodeField = new TextField("Stock Code");
    private final TextField descriptionField = new TextField("Description");
//    private final MultiSelectComboBox<Category> categoryBox = new MultiSelectComboBox<>("Categories");

    @Autowired
    public ProductView(GenericService<Product, String> productService) {
        this.productService = productService;
//        this.categoryService = categoryService;

        Button saveButton = new Button("Save", e -> saveProduct());
        FormLayout form = new FormLayout();
        form.add(stockCodeField, descriptionField, saveButton);

//        categoryBox.setItems(categoryService.findAll());
//        categoryBox.setItemLabelGenerator(Category::getName);

        add(form, grid);
        loadAllProducts();
    }

    private void loadAllProducts() {
        List<Product> products = productService.findAll();
        grid.setItems(products);
    }

    private void saveProduct() {
        Product product = new Product();
        product.setStockCode(stockCodeField.getValue());
        product.setDescription(descriptionField.getValue());

//        Set<Category> categories = new HashSet<>(categoryBox.getSelectedItems());
//        product.setCategories(categories);

        productService.save(product);
        loadAllProducts();
    }
}
