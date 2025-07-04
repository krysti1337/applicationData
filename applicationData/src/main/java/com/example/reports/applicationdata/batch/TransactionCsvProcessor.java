package com.example.reports.applicationdata.batch;

import com.example.reports.applicationdata.exception.ValidationException;
import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.model.Product;
import com.example.reports.applicationdata.model.Transaction;
import lombok.Getter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Component
public class TransactionCsvProcessor implements ItemProcessor<TransactionCsvRecord, Transaction> {

    @Getter
    private final Set<Customer> customers = new HashSet<>();

    @Getter
    private final Set<Product> products = new HashSet<>();

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");

    @Override
    public Transaction process(TransactionCsvRecord record) throws ValidationException {
        System.out.println("Procesăm record: " + record);

        validateRecord(record);

        // Construim Customer manual
        Customer customer = new Customer();
        customer.setCustomerId(Long.parseLong(record.getCustomerID()));
        customer.setCountry(record.getCountry());

        // Construim Product manual
        Product product = new Product();
        product.setStockCode(record.getStockCode());
        product.setDescription(record.getDescription());
        product.setUnitPrice(record.getUnitPrice());

        // Adaugam in colectie pentru tasklet
        customers.add(customer);
        products.add(product);

        // Construim obiectul Transaction
        Transaction transaction = new Transaction();
        transaction.setInvoiceNo(String.valueOf(record.getInvoiceNo()));
        transaction.setQuantity(record.getQuantity());
        transaction.setInvoiceDate(LocalDateTime.parse(record.getInvoiceDate(), formatter));
        transaction.setCustomer(customer);
        transaction.setProduct(product);

        return transaction;
    }

    public void validateRecord(TransactionCsvRecord record) {
        StringBuilder errors = new StringBuilder();

        if (record.getInvoiceNo() == null || record.getInvoiceNo().toString().isEmpty())
            errors.append("InvoiceNo is missing. ");

        if (record.getQuantity() <= 0)
            errors.append("Quantity must be positive. ");

        if (record.getInvoiceDate() == null || !isValidDate(record.getInvoiceDate()))
            errors.append("InvoiceDate is invalid. ");

        if (record.getCustomerID() == null || record.getCustomerID().isEmpty())
            errors.append("CustomerID is missing. ");

        if (record.getStockCode() == null || record.getStockCode().isEmpty())
            errors.append("StockCode is missing. ");

        if (errors.length() > 0)
            throw new ValidationException("Validation failed: " + errors);
    }

    private boolean isValidDate(String dateStr) {
        try {
            LocalDateTime.parse(dateStr, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}