package it.twinsbrain.dojos;

import it.twinsbrain.dojos.model.Balance;
import it.twinsbrain.dojos.model.Deposit;
import it.twinsbrain.dojos.model.Transaction;
import it.twinsbrain.dojos.model.Withdraw;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InMemoryAccountService implements AccountService {
  private final Balance balance;
  private final Time time;
  private final Display display;

  private final List<Transaction> transactionList = new ArrayList<>();
  private final ReadWriteLock lockOnWrite = new ReentrantReadWriteLock(true);

  public InMemoryAccountService(Balance balance, Time time, Display display) {
    this.balance = balance;
    this.time = time;
    this.display = display;
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
    atomically(
        (balance, transactionList) -> {
          display.show("Date       || Amount || Balance");
          var balanceReversedQueue = new LinkedList<Integer>();
          balanceReversedQueue.add(balance.value());
          transactionList.stream()
              .sorted(Comparator.comparing(Transaction::time).reversed())
              .peek(
                  m -> {
                    var prev = balanceReversedQueue.getLast();
                    var newBalance =
                        switch (m) {
                          case Deposit deposit -> prev - deposit.amount();
                          case Withdraw withdraw -> prev + withdraw.amount();
                        };
                    balanceReversedQueue.add(newBalance);
                  })
              .forEach(
                  transaction -> {
                    var amount =
                        switch (transaction) {
                          case Deposit deposit -> String.valueOf(deposit.amount());
                          case Withdraw withdraw -> "-" + withdraw.amount();
                        };
                    var message =
                        padRight(FORMATTER.format(transaction.time()), 11)
                            + "|| "
                            + padRight(amount, 7)
                            + "|| "
                            + balanceReversedQueue.pollFirst();
                    display.show(message);
                  });
        });
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

  private static String padRight(String s, int n) {
    return String.format("%-" + n + "s", s);
  }

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  @FunctionalInterface
  private interface AccountUpdater {
    void update();
  }

  @FunctionalInterface
  private interface AccountReader {
    void read(Balance balance, List<Transaction> transactionList);
  }
}
