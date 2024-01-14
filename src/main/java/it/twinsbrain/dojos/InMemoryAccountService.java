package it.twinsbrain.dojos;

import it.twinsbrain.dojos.model.Balance;
import it.twinsbrain.dojos.model.Deposit;
import it.twinsbrain.dojos.model.Movement;
import it.twinsbrain.dojos.model.Withdraw;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InMemoryAccountService implements AccountService {
  private final Balance balance;
  private final Time time;
  private final Display display;

  private final List<Movement> movementList = new ArrayList<>();
  private final Lock lockOnWrite = new ReentrantLock(true);

  public InMemoryAccountService(Balance balance, Time time, Display display) {
    this.balance = balance;
    this.time = time;
    this.display = display;
  }

  @Override
  public void deposit(int amount) {
    try {
      lockOnWrite.lock();
      balance.increaseBy(amount);
      movementList.add(new Deposit(amount, time.now()));
    } finally {
      lockOnWrite.unlock();
    }
  }

  @Override
  public void withdraw(int amount) {
    try {
      lockOnWrite.lock();
      balance.decreaseBy(amount);
      movementList.add(new Withdraw(amount, time.now()));
    } finally {
      lockOnWrite.unlock();
    }
  }

  @Override
  public void printStatement() {
    display.print("Date       || Amount || Balance");
    var balanceReversedQueue = new LinkedList<Integer>();
    balanceReversedQueue.add(balance.value());
    movementList.stream()
        .sorted(Comparator.comparing(Movement::time).reversed())
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
            movement -> {
              var amount =
                  switch (movement) {
                    case Deposit deposit -> String.valueOf(deposit.amount());
                    case Withdraw withdraw -> "-" + withdraw.amount();
                  };
              var message =
                  padRight(FORMATTER.format(movement.time()), 11)
                      + "|| "
                      + padRight(amount, 7)
                      + "|| "
                      + balanceReversedQueue.pollFirst();
              display.print(message);
            });
  }

  private static String padRight(String s, int n) {
    return String.format("%-" + n + "s", s);
  }

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
}
