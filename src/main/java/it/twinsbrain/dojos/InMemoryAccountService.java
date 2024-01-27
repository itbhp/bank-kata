package it.twinsbrain.dojos;

import it.twinsbrain.dojos.model.Balance;
import it.twinsbrain.dojos.model.Deposit;
import it.twinsbrain.dojos.model.Transaction;
import it.twinsbrain.dojos.model.Withdraw;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InMemoryAccountService implements AccountService {
  private final Balance balance;
  private final Time time;

  private final StatementPrinter statementPrinter;

  private final List<Transaction> transactionList = new ArrayList<>();
  private final ReadWriteLock lockOnWrite = new ReentrantReadWriteLock(true);

  public InMemoryAccountService(Balance balance, Time time, Display display) {
    this.balance = balance;
    this.time = time;
    this.statementPrinter = new OnlyTextStatementPrinter(display);
  }

  @Override
  public void deposit(int amount) {
    atomically(
        () -> {
          balance.increaseBy(amount);
          transactionList.add(new Deposit(amount, time.now()));
        });
  }

  @Override
  public void withdraw(int amount) {
    atomically(
        () -> {
          balance.decreaseBy(amount);
          transactionList.add(new Withdraw(amount, time.now()));
        });
  }

  @Override
  public void printStatement() {
    atomically(statementPrinter::printStatement);
  }

  private void atomically(AccountUpdater action) {
    Lock writeLock = lockOnWrite.writeLock();
    writeLock.lock();
    try {
      action.update();
    } finally {
      writeLock.unlock();
    }
  }

  private void atomically(AccountReader action) {
    Lock readLock = lockOnWrite.readLock();
    readLock.lock();
    try {
      action.read(balance, transactionList);
    } finally {
      readLock.unlock();
    }
  }
  
  @FunctionalInterface
  private interface AccountUpdater {
    void update();
  }

  @FunctionalInterface
  private interface AccountReader {
    void read(Balance balance, List<Transaction> transactionList);
  }
}
