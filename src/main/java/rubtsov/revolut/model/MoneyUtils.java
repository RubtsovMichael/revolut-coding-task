package rubtsov.revolut.model;

import java.math.BigDecimal;

public class MoneyUtils {

    public static BigDecimal setScale(BigDecimal amount) {
        return amount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
