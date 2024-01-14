package it.twinsbrain.dojos.model;

import java.time.LocalDateTime;

public record Withdraw(int amount, LocalDateTime time) implements Transaction {

}
