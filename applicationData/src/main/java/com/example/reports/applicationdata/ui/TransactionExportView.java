package com.example.reports.applicationdata.ui;

import com.example.reports.applicationdata.model.Transaction;
import com.example.reports.applicationdata.service.GenericService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Route("/export-transactions")
@Component
public class TransactionExportView extends VerticalLayout {

    private final GenericService<Transaction, Long> transactionService;
    private final Grid<Transaction> grid = new Grid<>(Transaction.class);

    @Autowired
    public TransactionExportView(GenericService<Transaction, Long> transactionService) {
        this.transactionService = transactionService;

        Button exportButton = new Button("Export CSV", e -> exportToCsv());

        add(exportButton, grid);
        loadTransactions();
    }

    private void loadTransactions() {
        List<Transaction> transactions = transactionService.findAll();
        grid.setItems(transactions);
    }

    private void exportToCsv() {
        List<Transaction> transactions = transactionService.findAll();
        try (FileWriter writer = new FileWriter("exported_transactions.csv")) {
            writer.write("InvoiceNo,Quantity,InvoiceDate,UnitPrice\n");
            for (Transaction t : transactions) {
                writer.write(String.format("%s,%d,%s,%.2f\n",
                        t.getInvoiceNo(),
                        t.getQuantity(),
                        t.getInvoiceDate(),
                        t.getUnitPrice()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}