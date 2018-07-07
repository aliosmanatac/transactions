package application.calculator;

import application.service.TransactionServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class AsyncStatisticsCalculatorTest {

    @Mock
    private TransactionServiceImpl transactionServiceImpl;

    @InjectMocks
    private AsyncStatisticsCalculator asyncStatisticsCalculator;

    @Test
    public void calculateStatisticsAsync_callsCalculateStats() {
        asyncStatisticsCalculator.calculateStatisticsAsync();
        verify(transactionServiceImpl).calculateStatistics();
    }
}