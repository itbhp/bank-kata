package it.twinsbrain.dojos;

import java.time.LocalDateTime;

public record Withdraw(int amount, LocalDateTime time) implements Movement{

}
