package rubtsov.revolut.model;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static rubtsov.revolut.model.MoneyUtils.setScale;

public class Account {

    private final String number;
    private BigDecimal amount;

    public Account(String number, BigDecimal amount) {
        this.number = number;
        this.amount = setScale(amount);
    }

    public String getNumber() {
        return number;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    private final Lock lock = new ReentrantLock();

    public void payIn(BigDecimal transferAmount) {
        amount = amount.add(setScale(transferAmount));
    }

    public void payOut(BigDecimal transferAmount) {
        amount = amount.subtract(setScale(transferAmount));
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
