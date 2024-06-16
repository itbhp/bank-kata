package dojo.bank.kata;

import java.time.LocalDateTime;


public record Withdrawal(int amount, LocalDateTime timestamp) implements Transaction {

}
