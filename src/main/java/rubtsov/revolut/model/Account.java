package rubtsov.revolut.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Account {
    private final String number;
    private BigDecimal amount;

    public void payIn(BigDecimal transferAmount) {
        amount = amount.add(transferAmount);
    }

    public void payOut(BigDecimal transferAmount) {
        amount = amount.subtract(transferAmount);
    }
}
