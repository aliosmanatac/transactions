package application.service;

import application.data.Statistics;
import application.data.Transaction;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionServiceImplTest {

    @Mock
    private TimeServiceImpl timeServiceImpl;

    @InjectMocks
    private TransactionServiceImpl transactionServiceImpl;

    private static final double AMOUNT1 = 12.3;
    private static final double AMOUNT2 = 56.7;

    private static final Transaction TRANSACTION1 = Transaction.builder()
            .amount(AMOUNT1)
            .timestamp(45000L)
            .build();
    private static final Transaction TRANSACTION2 = Transaction.builder()
            .amount(AMOUNT2)
            .timestamp(15000L)
            .build();

    private static final double DELTA = 0.01;

    @Test
    public void addTransaction_transactionNoOlderThan60Secs_returnsTrue() {
        when(timeServiceImpl.getCurrentTimeMillis()).thenReturn(105000L);
        boolean transactionResult = transactionServiceImpl.addTransaction(TRANSACTION1);
        assertTrue(transactionResult);
    }

    @Test
    public void addTransaction_transactionOlderThan60Secs_returnsFalse() {
        when(timeServiceImpl.getCurrentTimeMillis()).thenReturn(105001L);
        boolean transactionResult = transactionServiceImpl.addTransaction(TRANSACTION1);
        assertFalse(transactionResult);
    }

    @Test
    public void GetStatistics_withoutCalculate_returnsAllZeroStats() {
        when(timeServiceImpl.getCurrentTimeMillis()).thenReturn(105000L);
        transactionServiceImpl.addTransaction(TRANSACTION1);

        Statistics statistics = transactionServiceImpl.getStatistics();
        assertEquals(0.0, statistics.getSum(), DELTA);
        assertEquals(0.0, statistics.getMax(), DELTA);
        assertEquals(0.0, statistics.getMin(), DELTA);
        assertEquals(0.0, statistics.getAvg(), DELTA);
        assertEquals(0, statistics.getCount());
    }

    @Test
    public void GetStatistics_withCalculate_returnsActualStats() {
        when(timeServiceImpl.getCurrentTimeMillis()).thenReturn(75000L);
        transactionServiceImpl.addTransaction(TRANSACTION1);
        transactionServiceImpl.addTransaction(TRANSACTION2);
        transactionServiceImpl.calculateStatistics();
        Statistics statistics = transactionServiceImpl.getStatistics();
        assertEquals(AMOUNT1 + AMOUNT2, statistics.getSum(), DELTA);
        assertEquals(AMOUNT2, statistics.getMax(), DELTA);
        assertEquals(AMOUNT1, statistics.getMin(), DELTA);
        assertEquals((AMOUNT1 + AMOUNT2) / 2, statistics.getAvg(), DELTA);
        assertEquals(2, statistics.getCount());
    }

    @Test
    public void calculateStatistics_whenTransactionOlderThan60sec_removeTransactionFromResults() {
        when(timeServiceImpl.getCurrentTimeMillis()).thenReturn(75000L);
        transactionServiceImpl.addTransaction(TRANSACTION2);
        transactionServiceImpl.addTransaction(TRANSACTION1);
        when(timeServiceImpl.getCurrentTimeMillis()).thenReturn(75001L);
        transactionServiceImpl.calculateStatistics();
        Statistics statistics = transactionServiceImpl.getStatistics();
        assertEquals(AMOUNT1, statistics.getSum(), DELTA);
        assertEquals(AMOUNT1, statistics.getMax(), DELTA);
        assertEquals(AMOUNT1, statistics.getMin(), DELTA);
        assertEquals(AMOUNT1, statistics.getAvg(), DELTA);
        assertEquals(1, statistics.getCount());
    }
}