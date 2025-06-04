//package com.example.reports.applicationdata.ui;
//
//import com.example.reports.applicationdata.model.Customer;
//import com.example.reports.applicationdata.model.Order;
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
//@Route("/orders")
//@Component
//public class OrderView extends VerticalLayout {
//
//    private final GenericService<Order, Long> orderService;
//    private final GenericService<Customer, Long> customerService;
//    private final Grid<Order> grid = new Grid<>(Order.class);
//    private final TextField customerIdField = new TextField("Customer ID");
//    private final TextField orderNumberField = new TextField("Order Number");
//
//    @Autowired
//    public OrderView(GenericService<Order, Long> orderService, GenericService<Customer, Long> customerService) {
//        this.orderService = orderService;
//        this.customerService = customerService;
//
//        Button saveButton = new Button("Save", e -> saveOrder());
//        FormLayout form = new FormLayout();
//        form.add(customerIdField, orderNumberField, saveButton);
//
//        add(form, grid);
//        loadAllOrders();
//    }
//
//    private void loadAllOrders() {
//        List<Order> orders = orderService.findAll();
//        grid.setItems(orders);
//    }
//
//    private void saveOrder() {
//        Long customerId = Long.parseLong(customerIdField.getValue());
//        Customer customer = customerService.findById(customerId);
//
//        Order order = new Order();
//        order.setOrderNumber(orderNumberField.getValue());
//        order.setCustomer(customer);
//
//        orderService.save(order);
//        loadAllOrders();
//    }
//}
