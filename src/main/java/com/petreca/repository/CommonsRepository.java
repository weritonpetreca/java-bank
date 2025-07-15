package com.petreca.repository;

import com.petreca.exceptions.NotEnoughFundsException;
import com.petreca.model.Money;
import com.petreca.model.MoneyAudit;
import com.petreca.model.Wallet;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.petreca.model.BankService.ACCOUNT;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class CommonsRepository  {

    public static void checkFundsTransaction(final Wallet source, final long amount) {
        if (source.getFunds() < amount) {
            throw new NotEnoughFundsException("Not enough funds in the source wallet for this transaction.");
        }
    }

    public static List<Money> generateMoney(final UUID transactionId, final long funds, final String description) {
        var history = new MoneyAudit(transactionId, ACCOUNT, description, OffsetDateTime.now());
        return Stream.generate(() -> new Money(history)).limit(funds).toList();
    }
}
