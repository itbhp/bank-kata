package dojo.bank.kata;

import java.time.Clock;
import java.time.LocalDateTime;


public class DefaultAccountService implements AccountService {

    private final Display display;
    private final TransactionRepository transactionRepository;
    private final Clock clock;
    private final StatementPrinter statementPrinter;

    public DefaultAccountService(
        Display display,
        TransactionRepository repository,
        Clock clock
    ) {

        this.display = display;
        this.transactionRepository = repository;
        this.clock = clock;
        this.statementPrinter = new StatementPrinter(repository);
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
        statementPrinter.printOn(display);
    }

}
