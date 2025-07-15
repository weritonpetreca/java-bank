package com.petreca.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static com.petreca.model.BankService.ACCOUNT;

@Getter
public class AccountWallet extends Wallet{

    private final List<String> pix;
    private final List<InvestmentWallet> investmentWallets = new ArrayList<>();


    public AccountWallet(final long amount, final List<String> pix) {
        super(ACCOUNT);
        this.pix = pix;
        addMoney(amount, "Initial Deposit");
    }

    public void addMoney(final long amount, final String description) {
        var money = generateMoney(amount, description);
        this.money.addAll(money);
    }

    public List<InvestmentWallet> getInvestmentWallets() {
        return this.investmentWallets;
    }

    @Override
    public String toString() {
        return "AccountWallet{" +
                "Service = " + getService() +
                "; Funds = R$" + getFunds() / 100 + "," + getFunds() % 100 +
                "; Pix = " + pix +
                '}';
    }
}
