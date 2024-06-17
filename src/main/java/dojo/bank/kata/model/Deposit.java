package dojo.bank.kata.model;

import java.time.LocalDateTime;


public record Deposit(int amount, LocalDateTime timestamp) implements Transaction {

}
