package com.example.reports.applicationdata.service;

import com.example.reports.applicationdata.dao.impl.GenericDao;
import com.example.reports.applicationdata.model.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private GenericDao<Transaction, Long> transactionDao;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Test
    public void testSaveTransaction() {
        Transaction transaction = new Transaction();
        transaction.setInvoiceNo("INV-001");

        transactionService.save(transaction);

        verify(transactionDao).save(transaction);
    }

    @Test
    public void testFindTransactionById(){

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setInvoiceNo("INV-002");

        when(transactionDao.findById(1L)).thenReturn(transaction);

        Transaction result = transactionService.findById(1L);

        assertNotNull(result);
        assertEquals("INV-002", result.getInvoiceNo());
        verify(transactionDao).findById(1L);
    }

    @Test
    public void testFindAllTransactions(){
        List<Transaction> transactions = List.of(
                new Transaction(){{
                    setId(1L);
                    setInvoiceNo("INV-001");
                    setInvoiceDate(LocalDateTime.now());
                    setUnitPrice(new BigDecimal("100.00"));
                }},
                new Transaction(){{
                    setId(2L);
                    setInvoiceNo("INV-004");
                    setInvoiceDate(LocalDateTime.now());
                    setUnitPrice(new BigDecimal("200.00"));
                }}
        );

        when(transactionDao.findAll()).thenReturn(transactions);
        List<Transaction> result = transactionService.findAll();
        assertEquals(2, result.size());
        verify(transactionDao).findAll();
    }

    @Test
    public void testUpdateTransaction(){

        Transaction transaction = new Transaction();
        transaction.setId(3L);
        transaction.setInvoiceNo("INV-005");
        transactionService.save(transaction);

        transaction.setInvoiceNo("INV-006");
        transactionService.update(transaction);

        verify(transactionDao).update(transaction);
    }

    @Test
    public void testDeleteTransaction(){
        Transaction transaction = new Transaction();
        transaction.setId(4L);
        transaction.setInvoiceNo("INV-007");

        transactionService.save(transaction);
        transactionService.delete(transaction);

        verify(transactionDao).delete(transaction);
    }
}
