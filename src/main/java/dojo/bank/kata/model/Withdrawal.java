package dojo.bank.kata.model;

import java.time.LocalDateTime;


public record Withdrawal(int amount, LocalDateTime timestamp) implements Transaction {

}
