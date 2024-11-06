package com.funix.fx21990.asm4.model;

import com.funix.fx21990.asm4.dao.AccountDao;
import com.funix.fx21990.asm4.dao.CustomersDao;
import com.funix.fx21990.asm4.dao.TransactionDao;
import com.funix.fx21990.asm4.iface.IReport;
import com.funix.fx21990.asm4.iface.ITrasfer;
import com.funix.fx21990.asm4.service.BinaryFileService;
import com.funix.fx21990.asm4.utils.Utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Spliterator;


public class DigitalBank extends Bank {
    private List<Customer> customers = new ArrayList<>();
    private CustomersDao customersDao;
    private AccountDao accountDao;

    public DigitalBank() {
        customersDao = new CustomersDao();
        accountDao = new AccountDao();
        TransactionDao transactionDao = new TransactionDao();
    }

    @Override
    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public void showCustomers() throws IOException {
        List<Customer> customers = customersDao.list();
        if (customers.isEmpty()) {//isEmpty: khi chuỗi trống trả về true và ngược lại
            System.out.println("Khong tìm thấy khách hàng trong danh sách");
        }
        List<Account> accounts = accountDao.list();
        for (Customer customer : customers) {
            accounts.stream()
                    .filter(account -> customer.getCustomerId().equals(account.getCustomerID()))
                    .forEach(customer::addAccount);
        }

        customers.forEach(Customer::disPlayInformation);
    }

    public void addCustomers(String fileName) throws IOException {
        List<Customer> customers = customersDao.list();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) { // Đảm bảo dòng có đủ 2 phần
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    boolean exists = false;
                    for (Customer customer : customers) {
                        if (customer.getCustomerId() != null && customer.getCustomerId().equals(id)) {
                            System.out.println("Khách hàng đã tồn tại: " + id);
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        // Tạo đối tượng Customer và thêm vào danh sách
                        Customer customer = new Customer(id, name);
                        addCustomer(customer);
                        customers.add(customer);
                        System.out.println("Thêm khách hàng: " + id + " vào danh sách khách hàng");
                        customersDao.save(customers);
                    }
                } else {
                    System.out.println("Dữ liệu không đúng định dạng: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Không đọc được file: " + e.getMessage());
        }
    }

    public void addSavingAccount(Scanner scanner, String customerId) throws IOException {
        Customer customer = getCustomerById(customersDao.list(), customerId);
        if (customer != null) {
            String accountNumber = accountNumberFromUser();
            double balance = balanceFromUser();
            Account account = new SavingsAccount(customerId, balance);
            account.setAccountNumber(accountNumber);
            account.setBalance(balance);
            account.setType("SAVING");
            if (customer.getCustomerId().equals(customerId)) {
                List<Account> accounts = accountDao.list();
                if (isAccountExisted(accounts, account)) {
                    System.out.println("Tài khoản đã tồn tại");
                } else {
                    customer.addAccount(account);
                    accounts.add(account);
                    accountDao.save(accounts);
                    //  accountDao.update(account);
                    System.out.println("Thêm thành công");
                   // account.crateTransaction(balance, Utils.getDateTime(), true, "DEPOSIT");
                }
            }
        } else {
            System.out.println("Không tìm thấy khách hàng");
        }

    }

    private static String accountNumberFromUser() {
        System.out.println("Vui lòng nhập số tài khoản");
        Scanner scanner = new Scanner(System.in);
        String stk = scanner.nextLine();
        while (stk.length() != 6) {
            System.out.println("Số tài khoản phải gồm 6 chứ số");
            System.out.println("Vui lòng nhập lại");
            System.out.println("Hoac chon no de thoat");
            stk = scanner.nextLine();
            if (stk.equalsIgnoreCase("No")) {
                System.out.println("Bạn đã thoát");
                break;
            }
        }
        return stk;
    }

    private static double balanceFromUser() {
        while (true) {
            System.out.println("Nhap so tien ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                System.out.println("Vui long nhap so tien");
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

    //kiểm tra một account đã tồn tại trong mảng không.
    public boolean isAccountExisted(List<Account> accountList, Account newAccount) {
        for (Account account : accountList) {
            if (account.getAccountNumber().equals(newAccount.getAccountNumber())) {
                return true;
            }
        }
        return false;
    }

    public boolean isCustomerExisted(List<Customer> customers, Customer newCustomer) {
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(newCustomer.getCustomerId())) {
                return true;
            }
        }
        return false;
    }

    //lấy ra 1 customer có id bằng id cho
    public Customer getCustomerById(List<Customer> customerList, String customerId) {
        return customerList.stream()
                .filter(customer -> customer.getCustomerId().equals(customerId))
                .findFirst()
                .orElse(null);
    }

    public void withdraw(String customerId, String accountNumber, double amount) throws IOException {
        for (Customer customer : getCustomers()) {
            if (customer.getCustomerId().equals(customerId)) {
                ((DigitalCustomer) customer).withdraw(accountNumber, amount);
            }
        }
    }

    public void showTransaction() {
        List<Customer> customers = getCustomers();
        for (Customer customer : customers) {
            for (Account account : customer.getAccounts()) {
                for (Transation transation : account.getTransations()) {
                    if (transation.getAccountNumber().equals(account.getAccountNumber())) {
                        System.out.println(transation.toString());
                    }
                }
            }
        }
    }
}

