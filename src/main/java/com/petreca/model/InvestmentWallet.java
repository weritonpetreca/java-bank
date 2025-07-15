package com.petreca.model;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static com.petreca.model.BankService.INVESTMENT;

@Getter
public class InvestmentWallet extends Wallet{

    private final Investment investment;

    private final AccountWallet account;

    public InvestmentWallet(Investment investment, AccountWallet account, final long amount) {
        super(INVESTMENT);
        this.investment = investment;
        this.account = account;
        addMoney(account.reduceMoney(amount), getService(), "Investment deposit of: " + amount);
    }

    public void updateAmount(final long percentage) {
        var amount = getFunds() * percentage / 100;
        var history = new MoneyAudit(
                UUID.randomUUID(),
                getService(),
                "Investment update with " + percentage + "% increase",
                OffsetDateTime.now());
        var money = Stream.generate(() -> new Money(history))
                .limit(amount)
                .toList();
        this.money.addAll(money);
    }

    @Override
    public String toString() {
        return "InvestmentWallet{" +
                investment +
                ", Account =" + account +
                ", Service =" + getService() +
                ", Funds = R$" + getFunds() / 100 + "," + getFunds() % 100 +
                '}';
    }
}
