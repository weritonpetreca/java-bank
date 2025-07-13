package com.petreca.model;

import lombok.Getter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static com.petreca.model.BankService.INVESTMENT;

@ToString
@Getter
public class InvestmentWallet extends Wallet{

    private final Investment investment;

    private final AccountWallet account;

    public InvestmentWallet(Investment investment, AccountWallet account, final long amount) {
        super(INVESTMENT);
        this.investment = investment;
        this.account = account;
        addMoney(account.reduceMoney(amount), getService(), "Valor do Investimento");
    }

    public void updateAmount(final long percentage) {
        var amount = getFunds() * percentage / 100;
        var history = new MoneyAudit(
                UUID.randomUUID(),
                getService(),
                "Rendimentos",
                OffsetDateTime.now());
        var money = Stream.generate(() -> new Money(history))
                .limit(amount)
                .toList();
        this.money.addAll(money);
    }
}
