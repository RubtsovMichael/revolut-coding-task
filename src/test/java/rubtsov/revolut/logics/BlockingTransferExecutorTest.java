package rubtsov.revolut.logics;

import org.junit.jupiter.api.Test;
import rubtsov.revolut.model.Account;

import java.math.BigDecimal;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BlockingTransferExecutorTest {

    private final Account account1 = new Account("111", BigDecimal.TEN);
    private final Account account2 = new Account("222", BigDecimal.ONE);

    @Test
    void failsIfFirstAccountLocked() throws ExecutionException, InterruptedException {
        BlockingTransferExecutor blockingTransferExecutor = new BlockingTransferExecutor(account1, account2);

        lockAccount(account1);
        assertThatThrownBy(() -> blockingTransferExecutor.execute(() -> {}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Failed to lock account 111 because of concurrent transfer. Try again later.");
    }

    @Test
    void failsIfSecondAccountLocked() throws ExecutionException, InterruptedException {
        BlockingTransferExecutor blockingTransferExecutor = new BlockingTransferExecutor(account1, account2);

        lockAccount(account2);
        assertThatThrownBy(() -> blockingTransferExecutor.execute(() -> {}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Failed to lock account 222 because of concurrent transfer. Try again later.");

        //account1 should be released in finally
        lockAccount(account1);
    }

    @Test
    void unlocksAccountsInCaseOfFailure() throws ExecutionException, InterruptedException {
        BlockingTransferExecutor blockingTransferExecutor = new BlockingTransferExecutor(account1, account2);

        assertThatThrownBy(() -> blockingTransferExecutor.execute(() -> {throw new RuntimeException("qqq");}))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("qqq");

        //accounts should be released in finally
        lockAccount(account1);
        lockAccount(account2);
    }

    @Test
    void noDeadlock() throws ExecutionException, InterruptedException {
        BlockingTransferExecutor transferExecutor = new BlockingTransferExecutor(account1, account2);
        BlockingTransferExecutor reverseTransferExecutor = new BlockingTransferExecutor(account2, account1);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<?> future = executor.submit(() -> transferExecutor.execute(this::heavyTransaction));
        Future<?> reverseFuture = executor.submit(() -> reverseTransferExecutor.execute(this::heavyTransaction));

        future.get();
        reverseFuture.get();
    }

    private void heavyTransaction() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void allOperationsSynchronized() throws InterruptedException {
        ExecutorCompletionService<Object> executor = new ExecutorCompletionService<>(Executors.newFixedThreadPool(20));

        int paidIn = 100;
        for (int i = 0; i < paidIn; i++) {
            executor.submit(this::payInOneToAccount1);
        }

        int paidOut = 105;
        for (int i = 0; i < paidOut; i++) {
            executor.submit(this::payOutOneFromAccount1);
        }

        for (int i = 0; i < paidIn + paidOut; i++) {
            executor.take();
        }

        assertThat(account1.getAmount()).isEqualTo(new BigDecimal("5.00"));
    }

    private String payInOneToAccount1() {
        new BlockingTransferExecutor(account1, account2).execute(() -> account1.payIn(BigDecimal.ONE));
        return "";
    }

    private String payOutOneFromAccount1() {
        new BlockingTransferExecutor(account1, account2).execute(() -> account1.payOut(BigDecimal.ONE));
        return "";
    }

    private void lockAccount(Account account) throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> submit = executorService.submit(account::lock);
        submit.get();
    }

}