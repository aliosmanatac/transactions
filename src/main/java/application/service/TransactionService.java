package application.service;

import application.data.Statistics;
import application.data.Transaction;

public interface TransactionService {

    /**
     * Add transaction to the transactions if it is not older than 60 secs
     * @param transaction Transaction object to add to transactions
     * @return true if it is added to transactions; false otherwise
     */
    boolean addTransaction(Transaction transaction);

    /**
     * Get precalculated statistics as a Statistics object
     * Runs in O(1) since the statistics calculated by scheduled task beforehand
     * @return Statistics object that includes stats
     */
    Statistics getStatistics();

    /**
     * Calculate statistics from objects in the queue that occurred in the last 60sec.
     */
    void calculateStatistics();
}
