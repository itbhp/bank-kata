package it.twinsbrain.dojos;

import java.time.LocalDateTime;

public record Deposit(int amount, LocalDateTime time) implements Movement{
    
}
