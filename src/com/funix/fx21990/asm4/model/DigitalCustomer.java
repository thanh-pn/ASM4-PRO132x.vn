package com.funix.fx21990.asm4.model;

import java.io.IOException;

public class DigitalCustomer  extends Customer{

    public DigitalCustomer(String name, String customerId) {
        super(name, customerId);
    }

    public void withdraw(String accountNumber, double amount) throws IOException {
        for (Account account : getAccounts()) {
            if (account.getAccountNumber().equals(accountNumber)) {
                if (account instanceof SavingsAccount) {
                    ((SavingsAccount) account).withdraw(amount);
                }
            }
        }
        System.out.println("Rut tien that bai");
    }
}

