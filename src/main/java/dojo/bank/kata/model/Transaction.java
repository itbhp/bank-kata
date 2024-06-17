package dojo.bank.kata.model;

import java.time.LocalDateTime;


public sealed interface Transaction permits Deposit, Withdrawal{

  int amount();
  LocalDateTime timestamp();
}
