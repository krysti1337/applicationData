package com.example.reports.applicationdata.controller;

import com.example.reports.applicationdata.model.Transaction;
import com.example.reports.applicationdata.service.TransactionServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionServiceImpl transactionService;

    public TransactionController(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transaction")
    public List<Transaction> getAllTransactions() {
        return transactionService.findAll();
    }

    @GetMapping("/transaction/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return transactionService.findById(id);
    }

    @PostMapping("/transaction/save")
    public void saveTransaction(@RequestBody Transaction transaction) {
        transactionService.save(transaction);
    }

    @PutMapping("/transaction/update/{id}")
    public void updateTransaction(@PathVariable Long id, @RequestBody Transaction transaction) {
        transaction.setId(id);
        transactionService.update(transaction);
    }

    @DeleteMapping("/transaction/delete/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        Transaction transaction = transactionService.findById(id);
        if (transaction != null) {
            transactionService.delete(transaction);
        }
    }
}