package com.example.reports.applicationdata.ui;


import com.example.reports.applicationdata.batch.ExportTransactionTasklet;
import com.example.reports.applicationdata.model.Transaction;
import com.example.reports.applicationdata.service.GenericService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Route("/export-transactions")
@Component
public class TransactionExportView extends VerticalLayout {

    private final GenericService<Transaction, Long> transactionService;
    private final Grid<Transaction> grid = new Grid<>(Transaction.class);

    private final TextField invoiceNoField = new TextField("Invoice No");
    private final TextField countryField = new TextField("Country");
    private final TextField stockCodeField = new TextField("Stock Code");
    private final TextField descriptionField = new TextField("Description");

    private List<Transaction> allTransactions;

    @Autowired
    private ExportTransactionTasklet exportTasklet;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("exportTransactionJob")
    private Job exportTransactionJob;

    @Autowired
    public TransactionExportView(GenericService<Transaction, Long> transactionService) {
        this.transactionService = transactionService;

        Button filterButton = new Button("Filter", e -> applyFilters());
        Button resetButton = new Button("Reset", e -> resetFilters());
        Button exportButton = new Button("Export CSV (Batch)", e -> exportToCsv());

        HorizontalLayout filters = new HorizontalLayout(
                invoiceNoField, countryField, stockCodeField, descriptionField,
                filterButton, resetButton, exportButton
        );

        configureGrid();

        allTransactions = transactionService.findAll();
        grid.setItems(allTransactions);

        add(filters, grid);
    }

    private void configureGrid() {
        grid.removeAllColumns();
        grid.addColumn(Transaction::getInvoiceNo).setHeader("Invoice No");
        grid.addColumn(Transaction::getQuantity).setHeader("Quantity");
        grid.addColumn(Transaction::getInvoiceDate).setHeader("Invoice Date");
        grid.addColumn(t -> t.getCustomer().getCustomerId()).setHeader("Customer ID");
        grid.addColumn(t -> t.getCustomer().getCountry()).setHeader("Country");
        grid.addColumn(t -> t.getProduct().getStockCode()).setHeader("Stock Code");
        grid.addColumn(t -> t.getProduct().getDescription()).setHeader("Description");
        grid.addColumn(t -> t.getProduct().getUnitPrice()).setHeader("Unit Price");
    }

    private void applyFilters() {
        String invoiceNo = invoiceNoField.getValue().trim().toLowerCase();
        String country = countryField.getValue().trim().toLowerCase();
        String stockCode = stockCodeField.getValue().trim().toLowerCase();
        String description = descriptionField.getValue().trim().toLowerCase();

        List<Transaction> filtered = allTransactions.stream()
                .filter(t -> invoiceNo.isEmpty() || t.getInvoiceNo().toLowerCase().contains(invoiceNo))
                .filter(t -> country.isEmpty() || t.getCustomer().getCountry().toLowerCase().contains(country))
                .filter(t -> stockCode.isEmpty() || t.getProduct().getStockCode().toLowerCase().contains(stockCode))
                .filter(t -> description.isEmpty() || t.getProduct().getDescription().toLowerCase().contains(description))
                .collect(Collectors.toList());

        grid.setItems(filtered);
    }

    private void resetFilters() {
        invoiceNoField.clear();
        countryField.clear();
        stockCodeField.clear();
        descriptionField.clear();
        grid.setItems(allTransactions);
    }

    private void exportToCsv() {
        List<Transaction> filtered = grid.getListDataView().getItems().collect(Collectors.toList());
        exportTasklet.setTransactions(filtered);

        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis()) // necesar pentru re-rulare
                    .toJobParameters();

            jobLauncher.run(exportTransactionJob, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}