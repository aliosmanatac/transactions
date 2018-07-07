package application.calculator;

import application.service.TransactionService;
import application.service.TransactionServiceImpl;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AsyncStatisticsCalculator {
    private TransactionService transactionService;
    private final static long ONE_SECOND = 1000L;

    @Autowired
    public AsyncStatisticsCalculator(@NonNull final TransactionServiceImpl transactionServiceImpl) {
        this.transactionService = transactionServiceImpl;
    }

    @Scheduled(fixedRate = ONE_SECOND)
    public void calculateStatisticsAsync() {
        transactionService.calculateStatistics();
    }
}
