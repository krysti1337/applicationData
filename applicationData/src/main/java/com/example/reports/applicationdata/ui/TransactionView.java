package com.example.reports.applicationdata.ui;

import com.example.reports.applicationdata.model.Transaction;
import com.example.reports.applicationdata.service.GenericService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Route("/transactions")
@Component
public class TransactionView extends VerticalLayout {

    private final GenericService<Transaction, Long> transactionService;
    private final Grid<Transaction> grid = new Grid<>(Transaction.class);
    private final TextField filterField = new TextField("Filter by Invoice No");

    @Autowired
    public TransactionView(GenericService<Transaction, Long> transactionService) {
        this.transactionService = transactionService;

        filterField.setPlaceholder("Enter invoice number...");
        Button filterButton = new Button("Filter", event -> filterTransactions());
        Button resetButton = new Button("Reset", event -> loadAllTransactions());

        add(filterField, filterButton, resetButton, grid);
        loadAllTransactions();
    }

    private void loadAllTransactions() {
        List<Transaction> transactions = transactionService.findAll();
        grid.setItems(transactions);
    }

    private void filterTransactions() {
        String keyword = filterField.getValue();
        List<Transaction> filtered = transactionService.findAll().stream()
                .filter(t -> t.getInvoiceNo() != null && t.getInvoiceNo().contains(keyword))
                .toList();
        grid.setItems(filtered);
    }
}
