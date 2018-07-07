package application.service;

import application.data.Statistics;
import application.data.Transaction;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

@Component
public class TransactionServiceImpl implements TransactionService {
    private TimeService timeService;
    private static final long SIXTY_SECONDS = 60000L;
    private final PriorityBlockingQueue<Transaction> transactions = new PriorityBlockingQueue<>();
    private final ConcurrentLinkedQueue<Transaction> pendingTransactions = new ConcurrentLinkedQueue<>();

    private double sum, max, min, avg;
    private int count;

    @Autowired
    public TransactionServiceImpl(@NonNull final TimeServiceImpl timeServiceImpl) {
        this.timeService = timeServiceImpl;
        resetStats();
    }

    /**
     * Add transaction to the pendingTransactions if it is not older than 60 secs
     * Runs in O(1) complexity
     * @param transaction Transaction object to add to the queue
     * @return true if it is added to transactions; false otherwise
     */
    @Override
    public boolean addTransaction(@NonNull final Transaction transaction) {
        if (timeService.getCurrentTimeMillis() - transaction.getTimestamp() > SIXTY_SECONDS) {
            return false;
        }
        pendingTransactions.add(transaction);
        return true;
    }

    /**
     * Get precalculated statistics as a Statistics object
     * Runs in O(1) since the statistics calculated by scheduled task beforehand
     * @return Statistics object that includes stats
     */
    @Override
    public Statistics getStatistics() {
        synchronized (this) {
            return Statistics.builder().sum(sum).min(min).max(max).avg(avg).count(count).build();
        }
    }

    /**
     * Runs in 3 steps:
     * 1- Add pending tasks from pendingTransactions to transactions
     * 2- Remove all transactions that are older than 60 seconds
     * 3- Calculate statistics from remaining objects in the queue.
     * Runs in O(nlogn) in worst case (when all the objects are older than 60 seconds)
     */
    @Override
    public void calculateStatistics() {
        // Add pending transactions to the transactions queue
        Transaction pendingTransaction;
        while((pendingTransaction = pendingTransactions.poll()) != null) {
            transactions.add(pendingTransaction);
        }

        // Remove any transaction that is older than 60 secs
        while(transactions.size() > 0 && timeService.getCurrentTimeMillis() - transactions.peek().getTimestamp() > SIXTY_SECONDS) {
            transactions.poll();
        }

        double currSum, currMax, currMin;

        // Calculate statistics
        if(transactions.size() > 0) {
            Iterator iterator = transactions.iterator();
            currSum = 0;
            currMax = Double.MIN_VALUE;
            currMin = Double.MAX_VALUE;
            while (iterator.hasNext()) {
                Transaction currTransaction = (Transaction) iterator.next();
                currSum += currTransaction.getAmount();
                if(currMax < currTransaction.getAmount())
                    currMax = currTransaction.getAmount();
                if(currMin > currTransaction.getAmount())
                    currMin = currTransaction.getAmount();
            }
            updateStats(currSum, currMax, currMin, currSum / transactions.size(), transactions.size());
        } else {
            resetStats();
        }
    }

    private void updateStats(double sum, double max, double min, double avg, int count) {
        // Using synchronized block guarantees that this values will not be written and read at the same time
        synchronized (this) {
            this.sum = sum;
            this.max = max;
            this.min = min;
            this.avg = avg;
            this.count = count;
        }
    }
    private void resetStats() {
        updateStats(0.0, 0.0, 0.0, 0.0, 0);
    }
}
