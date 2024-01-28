package it.twinsbrain.dojos.domain;

import java.time.LocalDateTime;

public sealed interface Transaction permits Deposit, Withdraw {
    int amount();
    LocalDateTime time();
}
