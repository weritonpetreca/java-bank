package com.petreca.repository;

import com.petreca.exceptions.AccountNotFoundException;
import com.petreca.exceptions.PixInUseException;
import com.petreca.model.AccountWallet;
import com.petreca.model.MoneyAudit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.petreca.repository.CommonsRepository.checkFundsTransaction;
import static java.util.stream.Stream.*;

public class AccountRepository {

    private final List<AccountWallet> accounts = new ArrayList<>();

    public AccountWallet create(final List<String> pix, final long initialFunds){
        if (!accounts.isEmpty()) {
            var pixInUse = accounts.stream().flatMap(account -> account.getPix().stream()).toList();
            for (var pixToCheck : pix) {
                if (pixInUse.contains(pixToCheck)) {
                    throw new PixInUseException("The PIX " + pix + " is already in use.");
                }
            }
        }
        var newAccount = new AccountWallet(initialFunds, pix);
        accounts.add(newAccount);
        return newAccount;
    }

    public void deposit(final String pix, final long fundsAmount){
        var target = findByPix(pix);
        target.addMoney(fundsAmount, "Deposit by PIX: " + pix);
    }

    public long withdraw(final String pix, final long amount){
        var source = findByPix(pix);
        checkFundsTransaction(source, amount);
        source.reduceMoney(amount);
        return amount;
    }

    public void transferMoney(final String sourcePix, final String targetPix, final long amount){
        var source = findByPix(sourcePix);
        checkFundsTransaction(source, amount);
        var target = findByPix(targetPix);
        var message = "Transfer from " + sourcePix + " to " + targetPix + " of amount: " + amount;
        target.addMoney(source.reduceMoney(amount), source.getService(), message);

    }

    public AccountWallet findByPix(final String pix){
        return accounts.stream().filter(account -> account.getPix().contains(pix))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundException("Wallet with PIX " + pix + " doesn't exist or it was deleted."));
    }

    public List<AccountWallet> list() {
        return this.accounts;
    }

    public Map<LocalDateTime, List<MoneyAudit>> getHistory(String pix) {
        var account = findByPix(pix);
        // Stream of account transactions
        var accountTransactions = account.getFinancialTransactions().stream();

        // Stream of all investment transactions
        var investmentTransactions = account.getInvestmentWallets().stream()
                .flatMap(wallet -> wallet.getFinancialTransactions().stream());

        // Combine both streams and group by LocalDateTime
        return concat(accountTransactions, investmentTransactions)
                .collect(Collectors.groupingBy(
                        t -> t.createdAt().toLocalDateTime(),
                        TreeMap::new,
                        Collectors.toList()
                ));
    }
}
