package com.example.reports.applicationdata.ui;

import com.example.reports.applicationdata.model.Transaction;
import com.example.reports.applicationdata.service.GenericService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("transactions")
public class TransactionGridView extends VerticalLayout {

    private final GenericService<Transaction, Long> transactionService;
    private final Grid<Transaction> transactionGrid = new Grid<>(Transaction.class);

    @Autowired
    public TransactionGridView(GenericService<Transaction, Long> transactionService) {
        this.transactionService = transactionService;

        setSizeFull();
        configureGrid();
        add(transactionGrid);
        updateGrid();
    }

    private void configureGrid() {
        transactionGrid.addColumn(t -> t.getCustomer() != null ? t.getCustomer().getCustomerId() : "").setHeader("Customer ID");
        transactionGrid.addColumn(t -> t.getCustomer() != null ? t.getCustomer().getCountry() : "").setHeader("Country");
        transactionGrid.addColumn(t -> t.getProduct() != null ? t.getProduct().getStockCode() : "").setHeader("Stock Code");
        transactionGrid.addColumn(t -> t.getProduct() != null ? t.getProduct().getDescription() : "").setHeader("Description");
        transactionGrid.addColumn(t -> t.getProduct() != null ? t.getProduct().getUnitPrice() : "").setHeader("Unit Price");
    }

    private void updateGrid() {
        List<Transaction> transactions = transactionService.findAll();
        transactionGrid.setItems(transactions);
    }
}

