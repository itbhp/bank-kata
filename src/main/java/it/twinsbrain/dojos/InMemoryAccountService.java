package it.twinsbrain.dojos;

public class InMemoryAccountService implements AccountService {
  private final Balance balance;

  public InMemoryAccountService(Balance balance) {

    this.balance = balance;
  }

  @Override
  public void deposit(int amount) {
    balance.increaseBy(amount);
  }

  @Override
  public void withdraw(int amount) {}

  @Override
  public void printStatement() {}
}
