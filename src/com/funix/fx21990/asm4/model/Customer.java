package com.funix.fx21990.asm4.model;

import com.funix.fx21990.asm4.dao.AccountDao;
import com.funix.fx21990.asm4.dao.CustomersDao;

import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Customer extends User implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Account> accounts = new ArrayList<>();

    public Customer(String customerId, String name) {
        super(name, customerId);
        this.accounts = accounts;
    }

    public Customer(List<String> values) {
        this(values.get(0), values.get(1));
    }

    //lấy danh sách tài khoản của khách hàng
    public List<Account> getAccounts() {
        return AccountDao.list().stream()
                .filter(account -> account.getCustomerID().equals(this.getCustomerId()))
                .collect(Collectors.toList());
    }

    public String isPremium() {
        for (Account account : accounts) {
            if (account.isPremium()) {
                return "Premium";
            }
        }
        return "Normal";
    }

    //lấy account từ danh sách
    public Account getAccountByAccountNumber(List<Account> accounts, String accountNumber) {
        return accounts.stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }

    public void addAccount(Account newAccount) {
        if (accounts.isEmpty()) {
            accounts.add(newAccount);
//            System.out.println("Them tai khoan thanh cong");
        } else {
            List<String> accountNumbers = new ArrayList<>();
            for (Account account : accounts) {
                accountNumbers.add(account.getAccountNumber());
            }
            if (!accountNumbers.contains(newAccount.getAccountNumber())) {
                accounts.add(newAccount);
//                System.out.println("Them tai khoan thanh cong.");
            } else {
                System.out.println("Tai khoan da ton tai");
            }
        }
    }

    //Nhập số tài khoản (lấy danh sách accounts từ getAccounts để kiểm tra tài khoản có tồn tại hay không và gọi hàm rút tiền account
    public void withdraw(Scanner scanner) throws IOException {
        List<Account> accounts = getAccounts();
        if (!accounts.isEmpty()) {
            Account account;
            double amount;
            do {
                System.out.print("Nhập số tài khoản :");
                account = getAccountByAccountNumber(accounts, scanner.nextLine());
            } while (account == null);
            do {
                System.out.print("Nhập số tiền rút:  ");
                amount = Double.parseDouble(scanner.nextLine());
            } while (amount <= 0);
            if (account instanceof SavingsAccount) {
                ((SavingsAccount) account).withdraw(amount);
            }
        } else {
            System.out.println("Khách hàng không có tài khoản nào, thao tác không thành công");
        }
    }

    //nhập dúng tài khoản dể chuyển tiền, nhận và yêu cầu số tiền rút, xác nhạn chuyển tiền
    public double getBalance() {
        double sum = 0;
        for (Account account : accounts) {
            sum += account.getBalance();
        }
        return sum;
    }

    public void tranfers(Scanner scanner, String customerID) throws IOException {
        List<Account> accounts = AccountDao.list();
        String accountNumber = getvalidAccountNumber(scanner, accounts, customerID, true);
        String receiveAccount = getvalidAccountNumber(scanner, accounts, customerID, false);

        double balance = balanceFromUser();
        if (!isSufficientBalance(accounts, accountNumber, balance)) {
            System.out.println("Số dư không đủ thực hiện giao dịch. ");
            return;
        }
        confirmAndTransfer(scanner, accounts, accountNumber, receiveAccount, balance);
    }

    public String getvalidAccountNumber(Scanner scanner, List<Account> accounts, String customerID, boolean isSender) {
        String accountNumber = "";
        boolean validAccount = false;
        while (!validAccount) {
            System.out.print("Nhập số tài khoản " + (isSender ? "người gửi" : "người nhận ") + ": ");
            accountNumber = scanner.nextLine();

            for (Account account : accounts) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    if (isSender && account.getCustomerID().equals(customerID)) {
                        validAccount = true;
                    } else if (!isSender && !account.getCustomerID().equals(customerID)) {
                        validAccount = true;
                    } else {
                        System.out.println("Số tài khỏan không hợp lệ, vui lòng nhập lại");
                    }
                    break;
                }
            }
            if (!validAccount) {
                System.out.println("Số tài khoản không tồn tại, vui lòng nhập lại");
            }
        }
        return accountNumber;
    }

    public boolean isSufficientBalance(List<Account> accounts, String accountNumber, double amount) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account.getBalance() >= amount;
            }
        }
        return false;
    }

    public void confirmAndTransfer(Scanner scanner, List<Account> accounts, String accountNumber, String receiveAccount, double amount) throws IOException {
        System.out.print("Bạn có chắc chắn muốn chuyển " + amount + " từ tài khoản [" + accountNumber + "] đến tài khoản [" + receiveAccount + "]? Y/N: ");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("y")) {
            performTransfer(accounts, accountNumber, receiveAccount, amount);
        } else {
            System.out.println("Chuyển tiền bị hủy");
        }
    }

    public void performTransfer(List<Account> accounts, String accountNumber, String receiveAccount, double amount) throws IOException {
        Account senderAccount = null;
        Account receiverAccount = null;
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                senderAccount = account;
            } else if (account.getAccountNumber().equals(receiveAccount)) {
                receiverAccount = account;
            }
        }
        if (senderAccount instanceof SavingsAccount && receiverAccount instanceof SavingsAccount ) {
            try {
                ((SavingsAccount) senderAccount).transfer(receiverAccount,amount);
                ((SavingsAccount) senderAccount).logTransfer(amount,accountNumber,receiveAccount);

                ((SavingsAccount) receiverAccount).deposit(receiverAccount, amount);

            }catch (IOException e){
                System.err.println("Lỗi trong quá trình chuyển tiền" + e.getMessage());
            }
        }
    }

    public void disPlayInformation() {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        System.out.println(getCustomerId() + "  |          " + getName() + " | " + isPremium() + " |   "
                + formatter.format(getBalance()) + "đ");
        for (int index = 0; index < accounts.size(); index++) {
            System.out.println((index + 1) + "      " + accounts.get(index).toString());
        }
    }

    public String toString() {
        return "CustomerID: " + getCustomerId() + ", Name :" + getName() + ",  " + isPremium() + ",  " + getBalance() + "Đ";
    }

    private static double balanceFromUser() {
        while (true) {
            System.out.println("Nhap so tien :");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                System.out.println("Vui long nhap so tien :");
                continue;
            }
            try {
                double amount = Double.parseDouble(input);
                if (amount <= 50000) {
                    System.out.println("So tien toi thieu 50000");
                    continue;
                }
                return amount;
            } catch (NumberFormatException e) {
                System.out.println("Vui long nhap so tien hop le");
            }
        }
    }
}
