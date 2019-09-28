package rubtsov.revolut.model;

import java.math.BigDecimal;

import static rubtsov.revolut.model.MoneyUtils.setScale;

public class TransferOrder {
    private String from;
    private String to;
    private BigDecimal amount;

    public TransferOrder() {
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final TransferOrder transferOrder = new TransferOrder();

        public Builder from(String from) {
            transferOrder.setFrom(from);
            return this;
        }

        public Builder to(String to) {
            transferOrder.setTo(to);
            return this;
        }

        public Builder amount(BigDecimal amount) {
            transferOrder.setAmount(setScale(amount));
            return this;
        }

        public TransferOrder build() {
            return transferOrder;
        }
    }
}
