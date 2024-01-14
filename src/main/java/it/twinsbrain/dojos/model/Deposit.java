package it.twinsbrain.dojos.model;

import java.time.LocalDateTime;

public record Deposit(int amount, LocalDateTime time) implements Transaction {
    
}
