package dojo.bank.kata.services;

import java.time.Clock;
import java.time.LocalDateTime;

import dojo.bank.kata.repositories.TransactionRepository;
import dojo.bank.kata.model.Deposit;
import dojo.bank.kata.model.Withdrawal;


public class DefaultAccountService implements AccountService {

    private final Display display;
    private final TransactionRepository transactionRepository;
    private final Time time;
    private final StatementPrinter statementPrinter;

    public DefaultAccountService(
        Display display,
        TransactionRepository repository,
        Time clock
    ) {

        this.display = display;
        this.transactionRepository = repository;
        this.time = clock;
        this.statementPrinter = new StatementPrinter(repository);
    }

    @Override
    public void deposit(int amount) {
        transactionRepository.recordTransaction(
            new Deposit(
                amount,
                time.now()
            )
        );
    }

    @Override
    public void withdraw(int amount) {
        transactionRepository.recordTransaction(
            new Withdrawal(
                amount,
                time.now()
            )
        );
    }

    @Override
    public void printStatement() {
        statementPrinter.printOn(display);
    }

}
