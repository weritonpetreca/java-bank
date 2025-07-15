package com.petreca.repository;

import com.petreca.exceptions.AccountWithInvestmentException;
import com.petreca.exceptions.InvestmentNotFoundException;
import com.petreca.exceptions.WalletNotFoundException;
import com.petreca.model.AccountWallet;
import com.petreca.model.Investment;
import com.petreca.model.InvestmentWallet;

import java.util.ArrayList;
import java.util.List;

import static com.petreca.repository.CommonsRepository.checkFundsTransaction;

public class InvestmentRepository {

    private final List<Investment> investments = new ArrayList<>();

    private final List<InvestmentWallet> wallets = new ArrayList<>();

    private long nextId = 0;

    public Investment create(final long tax, final long initialFunds) {
        this.nextId++;
        var investment = new Investment(this.nextId, tax, initialFunds);
        investments.add(investment);
        return investment;
    }

    public InvestmentWallet initInvestment(final AccountWallet account, final long id) {
        if (!wallets.isEmpty()) {
            var accountsInUse = wallets.stream()
                    .map(InvestmentWallet::getAccount).toList();
            if (accountsInUse.contains(account)) {
                throw new AccountWithInvestmentException("The account '" + account + "' already has an investment wallet.");
            }
        }
        var investment = findById(id);
        checkFundsTransaction(account, investment.initialFunds());
        var wallet = new InvestmentWallet(investment, account, investment.initialFunds());
        wallets.add(wallet);
        account.getInvestmentWallets().add(wallet);
        return wallet;
    }

    public void deposit(final String pix, final long funds) {
            var wallet = findWalletByAccountPix(pix);
            wallet.addMoney(wallet.getAccount().reduceMoney(funds), wallet.getService(), "Deposit to investment the value of: " + funds);
    }

    public void withdraw(final String pix, final long funds) {
        var wallet = findWalletByAccountPix(pix);
        checkFundsTransaction(wallet, funds);
        wallet.getAccount().addMoney(wallet.reduceMoney(funds), wallet.getService(), "Withdrawal from investment the value of: " + funds);
        if (wallet.getFunds() == 0) {
            wallets.remove(wallet);
        }
    }

    public void updateAmount() {
        wallets.forEach(wallet -> wallet.updateAmount(wallet.getInvestment().tax()));
    }

    public Investment findById(final long id) {
        return investments.stream()
                .filter(investment -> investment.id() == id)
                .findFirst()
                .orElseThrow(() -> new InvestmentNotFoundException("Investment with ID " + id + " doesn't exist or it was deleted.")
                );
    }

    public InvestmentWallet findWalletByAccountPix(final String pix) {
        return wallets.stream()
                .filter(wallet -> wallet.getAccount().getPix().contains(pix))
                .findFirst()
                .orElseThrow(() -> new WalletNotFoundException("Wallet with PIX " + pix + " doesn't exist or it was deleted.")
                );
    }

    public List<InvestmentWallet> listWallets() {
        return this.wallets;
    }

    public List<Investment> list() {
        return this.investments;
    }
}
