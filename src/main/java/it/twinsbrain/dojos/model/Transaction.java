package it.twinsbrain.dojos.model;

import java.time.LocalDateTime;

public sealed interface Transaction permits Deposit, Withdraw {
    int amount();
    LocalDateTime time();
}
