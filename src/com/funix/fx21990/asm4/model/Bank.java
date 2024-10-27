package com.funix.fx21990.asm4.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Bank {
    private final String id;

    private final List<Customer> customers;

    public Bank() {
        customers = new ArrayList<>();
        this.id = String.valueOf(UUID.randomUUID());
    }

    public String getId() {
        return id;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void addCustomer(Customer newCustomer) {
        if (!isCustomerExisted(newCustomer.getCustomerId())) {
            customers.add(newCustomer);
        }
    }

    public boolean isCustomerExisted( String customerId) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId))
                return true;
        }
        return false;
    }

    public boolean isAccountNumberExisted(String accountNumber) {
        for (Customer cus : customers) {
            List<Account> accounts = cus.getAccounts();
            for (Account acc : accounts) {
                if (acc.getAccountNumber().equals(accountNumber)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void addAccount(String customerId, Account account) {
        for (Customer customer : customers) {
            if (isCustomerExisted(customerId) && customer.getCustomerId().equals(customerId)) {
                customer.addAccount(account);
            }
        }
    }
}
