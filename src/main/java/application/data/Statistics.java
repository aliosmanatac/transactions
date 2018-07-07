package application.data;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Statistics {
    private double sum, max, min, avg;
    private int count;
}
