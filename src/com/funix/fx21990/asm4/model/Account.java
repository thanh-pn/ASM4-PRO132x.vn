package com.funix.fx21990.asm4.model;

import com.funix.fx21990.asm4.dao.AccountDao;
import com.funix.fx21990.asm4.dao.TransactionDao;
import com.funix.fx21990.asm4.iface.ReportService;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Account implements Serializable, ReportService {

    //Serializable: hỗ trợ đọc/ ghi object
    private static final long serialVersionUID = 1L; // Đảm bảo serialVersionUID đúng
    private String customerID;
    private String accountNumber;
    private double balance;
    private String type;
    public List<Transation> transations = new ArrayList<>();

    public Account(String customerId, double balance) {
        this.customerID = customerId;
        this.balance = balance;
        this.transations = new ArrayList<>();
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setTransations(List<Transation> transations) {
        this.transations = transations;
    }

    public String getCustomerID() {
        return customerID;
    }

    //lấy danh sách giao dịch của tài khoản
    public List<Transation> getTransations() {
        return TransactionDao.list().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .collect(Collectors.toList());
    }

    public void addTransaction(Transation newTransation) {
        transations.add(newTransation);
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPremium() {
        return balance >= 10000000;
    }

    //tạo ra thêm một giao dịch cho account và cập nhật số dư tài khoản.
    public void crateTransaction(double amount, String time, boolean status, String type) throws IOException {
        Transation transation = new Transation(amount, time, status, type, accountNumber);
        TransactionDao.save(transations);
        //cạp nhat so du
        balance += amount;
        AccountDao.update(this);
    }

    public void input(Scanner scanner) {

    }

    public void displayTranSaction() {
        for (Transation transation : transations) {
            System.out.println(transation.toString());
        }
    }
    @Override
    public String toString() {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        if (type != null) {
            return accountNumber + " |        " + type + " |               " + formatter.format(balance) + "đ";
        }
        return accountNumber + " |        " + "     |            " + formatter.format(balance) + "đ";
    }
    @Override
    public void log(double amount) {

    }
}
