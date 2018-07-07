package application.data;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction implements Comparable<Transaction> {
    private double amount = 0;
    private long timestamp = 0;

    @Override
    public int compareTo(Transaction t2) {
        if(this.amount > t2.amount) {
            return -1;
        }
        if(this.amount < t2.amount) {
            return 1;
        }
        return 0;
    }
}
