package application.service;

import org.springframework.stereotype.Component;

@Component
public class TimeServiceImpl implements TimeService {
    @Override
    public long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }
}
