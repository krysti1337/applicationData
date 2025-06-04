package com.example.reports.applicationdata.batch;

import com.example.reports.applicationdata.model.Transaction;
import com.example.reports.applicationdata.service.GenericService;
import com.example.reports.applicationdata.service.TransactionServiceImpl;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class ExportTransactionTasklet implements Tasklet {

    private List<Transaction> transactions;

    @Autowired
    private GenericService<Transaction, Long> transactionService;

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        List<Transaction> toExport = this.transactions != null ? this.transactions : transactionService.findAll();

        String folderPath = "exports";
        String filePath = folderPath + "/exported_transactions.csv";

        try {
            File folder = new File(folderPath);
            if (!folder.exists() && !folder.mkdirs()) {
                throw new IOException("Could not create export directory");
            }

            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write("InvoiceNo,Quantity,InvoiceDate,CustomerID,Country,StockCode,Description,UnitPrice\n");
                for (Transaction t : toExport) {
                    writer.write(String.format("%s,%d,%s,%d,%s,%s,%s,%.2f\n",
                            t.getInvoiceNo(),
                            t.getQuantity(),
                            t.getInvoiceDate(),
                            t.getCustomer().getCustomerId(),
                            t.getCustomer().getCountry(),
                            t.getProduct().getStockCode(),
                            t.getProduct().getDescription(),
                            t.getProduct().getUnitPrice()));
                }
            }

            return RepeatStatus.FINISHED;
        } catch (IOException e) {
            throw new RuntimeException("Failed to export CSV", e);
        }
    }
}