package it.twinsbrain.dojos.domain;

import java.time.LocalDateTime;

public record Withdraw(int amount, LocalDateTime time) implements Transaction {

}
