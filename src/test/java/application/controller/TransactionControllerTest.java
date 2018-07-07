package application.controller;

import application.data.Statistics;
import application.data.Transaction;
import application.service.TransactionServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {

    @Mock
    private TransactionServiceImpl transactionServiceImpl;

    @InjectMocks
    private TransactionController transactionController;

    private static final Transaction TRANSACTION = Transaction.builder()
            .amount(123.2)
            .timestamp(10000L)
            .build();

    @Test
    public void transactions_whenAddTransactionSucceeds_returnsHttp201() {
        when(transactionServiceImpl.addTransaction(TRANSACTION)).thenReturn(true);
        ResponseEntity response = transactionController.transactions(TRANSACTION);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertFalse((response.hasBody()));
    }

    @Test
    public void transactions_whenAddTransactionFails_returnsHttp204() {
        when(transactionServiceImpl.addTransaction(TRANSACTION)).thenReturn(false);
        ResponseEntity response = transactionController.transactions(TRANSACTION);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertFalse((response.hasBody()));
    }

    @Test
    public void statistics_happyCase_returnsStatistics() {
        Statistics statistics = Statistics.builder().sum(12.2).max(7.0).min(5.2).avg(6.1).count(2).build();
        when(transactionServiceImpl.getStatistics()).thenReturn(statistics);
        ResponseEntity<Statistics> response = transactionController.statistics();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.hasBody());
        assertEquals(statistics, response.getBody());
    }
}