package it.twinsbrain.dojos.domain;

import java.time.LocalDateTime;

public record Deposit(int amount, LocalDateTime time) implements Transaction {
    
}
