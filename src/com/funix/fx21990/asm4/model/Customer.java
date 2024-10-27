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
    public void withdraw(Scanner scanner) {
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
        //số tài khoản người gửi
        String accountNumber = "";
        boolean validSenderAccount = false;
        while (!validSenderAccount) {
            System.out.print("Nhập số tài khoản");
            accountNumber = scanner.nextLine();
            for (Account account : accounts) {
                if (account.getCustomerID().equals(customerID)) {
                    if (account.getAccountNumber().equals(accountNumber)) {
                        validSenderAccount = true;
                        break;
                    }
                }
            }
            if (!validSenderAccount) {
                System.out.println("Số tài khoản không tồn tại , vui lòng nhập lại");
            }
        }
        //số tài khoản người nhận
        String receiveAccount = "";
        boolean validReceiveAccount = false;
        while (!validReceiveAccount) {
            System.out.print("Nhập số tài khoản người nhận :");
            receiveAccount = scanner.nextLine();
            for (Account account : accounts) {
                if (account.getAccountNumber().equals(receiveAccount)) {
                    if (account.getAccountNumber().equals(accountNumber)) {
                        System.out.println("Số tài khoản người gửi và người nhận không được trùng nhau");
                        break;
                    }
                    if (account.getCustomerID().equals(customerID)) {
                        System.out.println("Mã số khách hàng phải khác nhau");
                        break;
                    }
                    validReceiveAccount = true;
                    break;
                }
            }
            if (!validReceiveAccount) {
                System.out.println("Số tài khoản không tồn tại , vui lòng nhập lại");
            }
        }
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(receiveAccount)) {
                for (Customer customer : CustomersDao.list()) {
                    if (account.getCustomerID().equals(customer.getCustomerId())) {
                        System.out.println("Gửi số tiền đến tài khoản: " + receiveAccount + " | " + customer.getName());
                    }
                }
            }
        }
        double balance = balanceFromUser();
        System.out.print("Bạn có chắc chắn muốn chuyển" + balance + "từ tài khoản [" + accountNumber + "]" + "đến tài khoản [" + receiveAccount + "]" + "Y/N");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("y")) {
            for (Account account : accounts) {
                if (account.getAccountNumber().equals(accountNumber)) {
                    account.setType("TRANSFERS");
//                    if (account instanceof SavingsAccount) {
//                        ((SavingsAccount) account).transfer(account, balance);
//                    }
                } else if (account.getAccountNumber().equals(receiveAccount)) {
                    account.setType("DEPOSIT");
//                    if (account instanceof SavingsAccount) {
//                        ((SavingsAccount) account).transfer(account, balance);
//                    }
                }
            }
        } else {
            System.out.println("Chuyển tiền bị hủy");
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
