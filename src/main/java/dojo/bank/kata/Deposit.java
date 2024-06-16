package dojo.bank.kata;

import java.time.LocalDateTime;


public record Deposit(int amount, LocalDateTime timestamp) implements Transaction {

}
