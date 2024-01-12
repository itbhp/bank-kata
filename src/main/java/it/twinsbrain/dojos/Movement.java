package it.twinsbrain.dojos;

import java.time.LocalDateTime;

public sealed interface Movement permits Deposit, Withdraw {
    int amount();
    LocalDateTime time();
}
