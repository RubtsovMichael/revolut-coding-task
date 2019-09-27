package rubtsov.revolut.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    @Getter
    private final String number;
    @Getter
    private BigDecimal amount;

    public Account(String number, BigDecimal amount) {
        this.number = number;
        this.amount = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    private final Lock lock = new ReentrantLock();

    public void payIn(BigDecimal transferAmount) {
        amount = amount.add(transferAmount);
    }

    public void payOut(BigDecimal transferAmount) {
        amount = amount.subtract(transferAmount);
    }

    public void lock() {
        try {
            if (!lock.tryLock(3000, TimeUnit.MILLISECONDS)) {
                throw new IllegalArgumentException("Failed to lock account " + number + " because of concurrent transfer. Try again later.");
            }
        } catch (InterruptedException e) {
            throw new IllegalArgumentException("Unexpected interruption", e);
        }
    }

    public void unlock() {
        lock.unlock();
    }
}
