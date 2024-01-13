package it.twinsbrain.dojos;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    movementList.forEach(
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
                  + balance.value();
          display.print(message);
        });
  }

  private static String padRight(String s, int n) {
    return String.format("%-" + n + "s", s);
  }

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
}
