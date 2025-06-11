package dojo.bank.kata.services;

import dojo.bank.kata.model.Deposit;
import dojo.bank.kata.model.Withdrawal;
import dojo.bank.kata.repositories.TransactionRepository;


public class DefaultAccountService implements AccountService {

  private final TransactionRepository transactionRepository;
  private final Time time;
  private final StatementPrinter statementPrinter;

  public DefaultAccountService(
      Display display,
      TransactionRepository repository,
      Time clock
  ) {

    this.transactionRepository = repository;
    this.time = clock;
    this.statementPrinter = new StatementPrinter(repository, display);
  }

  @Override
  public void deposit(int amount) {
    transactionRepository.recordTransaction(
        new Deposit(amount, time.now())
    );
  }

  @Override
  public void withdraw(int amount) {
    transactionRepository.recordTransaction(
        new Withdrawal(amount, time.now())
    );
  }

  @Override
  public void printStatement() {
    statementPrinter.print();
  }

}
