package dojo.bank.kata;

import java.time.Clock;
import java.time.LocalDateTime;


public class DefaultAccountService implements AccountService {

    private final Display display;
    private final TransactionRepository transactionRepository;
    private final Clock clock;

    public DefaultAccountService(
        Display display,
        TransactionRepository transactionRepository,
        Clock clock
    ) {

        this.display = display;
        this.transactionRepository = transactionRepository;
        this.clock = clock;
    }

    @Override
    public void deposit(int amount) {
        transactionRepository.recordTransaction(
            new Deposit(
                amount,
                LocalDateTime.now(clock)
            )
        );
    }

    @Override
    public void withdraw(int amount) {
        transactionRepository.recordTransaction(
            new Withdrawal(
                amount,
                LocalDateTime.now(clock)
            )
        );
    }

    @Override
    public void printStatement() {

    }
}
