package dojo.bank.kata;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


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
}
