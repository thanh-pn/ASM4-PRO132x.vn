package com.funix.fx21990.asm4.dao;

import com.funix.fx21990.asm4.model.Account;
import com.funix.fx21990.asm4.service.BinaryFileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountDao {
    private final static String FILE_PATH = "store/account.dat";
    private final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);

    public static void save(List<Account> accounts) throws IOException {
        BinaryFileService.writeFile(FILE_PATH, accounts);
    }
    public static List<Account> list() {
        return BinaryFileService.readFile(FILE_PATH);
    }

    public static void update(Account editAccount) throws IOException {
        EXECUTOR_SERVICE.submit(() -> {
            try {
                List<Account> accounts = list();
                boolean hasExit = accounts.stream().anyMatch(account -> account.getAccountNumber().equals(editAccount.getAccountNumber()));

                List<Account> updateAccount;
                if (!hasExit) {
                    updateAccount = new ArrayList<>(accounts);
                    updateAccount.add(editAccount);
                } else {
                    updateAccount = new ArrayList<>();
                    for (Account account : accounts) {
                        if (account.getAccountNumber().equals(editAccount.getAccountNumber())) {
                            updateAccount.add(editAccount);
                        } else {
                            updateAccount.add(account);
                        }
                    }
                }
                save(updateAccount);
            } catch (IOException e) {
                System.out.println("Error updating account: " + e.getMessage());
            }
        });
    }

}
