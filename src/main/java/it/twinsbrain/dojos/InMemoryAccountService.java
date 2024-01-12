package it.twinsbrain.dojos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class InMemoryAccountService implements AccountService {
  private final Balance balance;
  private final List<Movement> movementList = new ArrayList<>();
  private final Lock lockOnWrite = new ReentrantLock(true);
  private final Time time;

  public InMemoryAccountService(Balance balance, Time time) {
    this.balance = balance;
    this.time = time;
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
  public void printStatement() {}
}
