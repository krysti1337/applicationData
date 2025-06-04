package com.example.reports.applicationdata.ui;

import com.example.reports.applicationdata.model.Transaction;
import com.example.reports.applicationdata.service.GenericService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route("/transactions")
public class TransactionGridView extends VerticalLayout {

    private final GenericService<Transaction, Long> transactionService;
    private final Grid<Transaction> transactionGrid = new Grid<>();

    @Autowired
    public TransactionGridView(GenericService<Transaction, Long> transactionService) {
        this.transactionService = transactionService;

        setSizeFull();
        configureGrid();
        add(transactionGrid);
        updateGrid();
    }

    private void configureGrid() {
        transactionGrid.setSizeFull();

        transactionGrid.addColumn(Transaction::getId).setHeader("Id");
        transactionGrid.addColumn(t -> t.getInvoiceNo()).setHeader("Invoice No");
        transactionGrid.addColumn(t -> t.getInvoiceDate().toString()).setHeader("Invoice Date");
        transactionGrid.addColumn(t -> t.getQuantity()).setHeader("Quantity");

        transactionGrid.addColumn(t -> {
            if (t.getCustomer() != null) return t.getCustomer().getCustomerId();
            return "";
        }).setHeader("Customer ID");

        transactionGrid.addColumn(t -> {
            if (t.getCustomer() != null) return t.getCustomer().getCountry();
            return "";
        }).setHeader("Country");

        transactionGrid.addColumn(t -> {
            if (t.getProduct() != null) return t.getProduct().getStockCode();
            return "";
        }).setHeader("Stock Code");

        transactionGrid.addColumn(t -> {
            if (t.getProduct() != null) return t.getProduct().getDescription();
            return "";
        }).setHeader("Description");

        transactionGrid.addColumn(t -> {
            if (t.getProduct() != null) return t.getProduct().getUnitPrice();
            return "";
        }).setHeader("Product Unit Price");
    }

    private void updateGrid() {
        List<Transaction> transactions = transactionService.findAll();
        transactionGrid.setItems(transactions);
    }
}

