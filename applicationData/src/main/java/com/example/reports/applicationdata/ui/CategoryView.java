//package com.example.reports.applicationdata.ui;
//
//import com.example.reports.applicationdata.model.Category;
//import com.example.reports.applicationdata.service.GenericService;
//import com.vaadin.flow.component.button.Button;
//import com.vaadin.flow.component.formlayout.FormLayout;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.component.orderedlayout.VerticalLayout;
//import com.vaadin.flow.component.textfield.TextField;
//import com.vaadin.flow.router.Route;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Route("/categories")
//@Component
//public class CategoryView extends VerticalLayout {
//
//    private final GenericService<Category, Long> categoryService;
//    private final Grid<Category> grid = new Grid<>(Category.class);
//    private final TextField nameField = new TextField("Category Name");
//
//    @Autowired
//    public CategoryView(GenericService<Category, Long> categoryService) {
//        this.categoryService = categoryService;
//
//        Button saveButton = new Button("Save", e -> saveCategory());
//        FormLayout form = new FormLayout();
//        form.add(nameField, saveButton);
//
//        add(form, grid);
//        loadAllCategories();
//    }
//
//    private void loadAllCategories() {
//        List<Category> categories = categoryService.findAll();
//        grid.setItems(categories);
//    }
//
//    private void saveCategory() {
//        Category category = new Category();
//        category.setName(nameField.getValue());
//        categoryService.save(category);
//        nameField.clear();
//        loadAllCategories();
//    }
//}