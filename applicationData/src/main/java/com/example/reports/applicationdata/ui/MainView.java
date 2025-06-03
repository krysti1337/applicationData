package com.example.reports.applicationdata.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.PWA;
import org.springframework.stereotype.Component;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        setSpacing(true);
        setPadding(true);

        add(new HorizontalLayout(
                new RouterLink("Transactions", TransactionGridView.class),
                new RouterLink("Customers", CustomerView.class),
                new RouterLink("Products", ProductView.class),
                new RouterLink("Categories", CategoryView.class),
                new RouterLink("Orders", OrderView.class),
                new RouterLink("Export", TransactionExportView.class)
        ));
    }
}