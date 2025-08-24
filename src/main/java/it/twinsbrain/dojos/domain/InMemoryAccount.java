package it.twinsbrain.dojos.domain;

import it.twinsbrain.dojos.Display;
import it.twinsbrain.dojos.Time;
import it.twinsbrain.dojos.adapters.OnlyTextStatementPrinter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InMemoryAccount implements AccountService {
  private final Balance balance;
  private final Time time;
  private final StatementPrinter statementPrinter;

  private final List<Transaction> transactionList = new ArrayList<>();
  private final ReadWriteLock lockOnWrite = new ReentrantReadWriteLock(true);

  public InMemoryAccount(Balance balance, Time time, Display display) {
    this.balance = balance;
    if (balance.value() > 0) {
      transactionList.add(new Deposit(balance.value(), time.now()));
    }
    if (balance.value() < 0) {
      transactionList.add(new Withdraw(balance.value(), time.now()));
    }
    this.time = time;
    this.statementPrinter = new OnlyTextStatementPrinter(display);
  }

  @Override
  public void deposit(int amount) {
    atomically(
        () -> {
          balance.increaseBy(amount);
          transactionList.add(new Deposit(amount, time.now()));
        }
    );
  }

  @Override
  public void withdraw(int amount) {
    atomically(
        () -> {
          balance.decreaseBy(amount);
          transactionList.add(new Withdraw(amount, time.now()));
        }
    );
  }

  @Override
  public void printStatement() {
    concurrently(statementPrinter::printStatement);
  }

  private void atomically(AccountUpdater updates) {
    var writeLock = lockOnWrite.writeLock();
    writeLock.lock();
    try {
      updates.execute();
    } finally {
      writeLock.unlock();
    }
  }

  private void concurrently(AccountInfoGatherer gatherInfo) {
    var readLock = lockOnWrite.readLock();
    readLock.lock();
    try {
      gatherInfo.from(balance, transactionList);
    } finally {
      readLock.unlock();
    }
  }

  @FunctionalInterface
  private interface AccountUpdater {
    void execute();
  }

  @FunctionalInterface
  private interface AccountInfoGatherer {
    void from(Balance balance, List<Transaction> transactionList);
  }
}
