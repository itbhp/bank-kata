package dojo.bank.kata.services;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dojo.bank.kata.model.Deposit;
import dojo.bank.kata.model.Withdrawal;
import dojo.bank.kata.repositories.TransactionRepository;


@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private Display display;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private Clock clock;

    @Test
    void deposit_record_a_proper_transaction() {
        // given
        given(clock.instant())
            .willReturn(Instant.parse("2021-07-01T00:00:00Z"));
        given(clock.getZone())
            .willReturn(Clock.systemUTC().getZone());

        // when
        var account = new DefaultAccountService(display, transactionRepository, clock);
        account.deposit(1000);

        // then
        verify(transactionRepository)
            .recordTransaction(
                new Deposit(
                    1000,
                    LocalDateTime.parse("2021-07-01T00:00:00")
                )
            );
    }

    @Test
    void withdraw_record_a_proper_transaction() {
        // given
        given(clock.instant())
            .willReturn(Instant.parse("2021-07-01T00:00:00Z"));
        given(clock.getZone())
            .willReturn(Clock.systemUTC().getZone());

        // when
        var account = new DefaultAccountService(display, transactionRepository, clock);
        account.withdraw(800);

        // then
        verify(transactionRepository)
            .recordTransaction(
                new Withdrawal(
                    800,
                    LocalDateTime.parse("2021-07-01T00:00:00")
                )
            );
    }

    @Test
    void print_statement_with_reversed_order_transaction_list() {
        // given
        given(transactionRepository.allTransactions())
            .willReturn(
                List.of(
                    new Deposit(1000, LocalDateTime.parse("2021-07-01T00:00:00")),
                    new Withdrawal(500, LocalDateTime.parse("2021-07-03T00:00:00"))
                )
            );

        // when
        var account = new DefaultAccountService(display, transactionRepository, clock);
        account.printStatement();

        // then
        var inOrder = inOrder(display);
        inOrder.verify(display).show("Date       || Amount || Balance");
        inOrder.verify(display).show("03/07/2021 || -500   || 500");
        inOrder.verify(display).show("01/07/2021 || 1000   || 1000");
    }
}
