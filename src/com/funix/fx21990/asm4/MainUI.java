package com.funix.fx21990.asm4;

import com.funix.fx21990.asm4.dao.AccountDao;
import com.funix.fx21990.asm4.dao.CustomersDao;
import com.funix.fx21990.asm4.model.*;
import com.funix.fx21990.asm4.service.BinaryFileService;
import com.funix.fx21990.asm4.service.TextFileService;

import javax.naming.BinaryRefAddr;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class MainUI {
    private static final DigitalBank activeBank = new DigitalBank();
    private static final Scanner scanner = new Scanner(System.in);
    public static final String TYPE_SAVINGS = "SAVINGS";


    public static void main(String[] args) {
        initUI();
    }

    private static void initUI() {
        boolean runAgain = false;
        try {
            System.out.println("+----------+----------------------+-----------+");
            System.out.println("| NGAN HANG SO | FX21990@v4.0.0               |");
            System.out.println("+----------+----------------------+-----------+");
            System.out.println("1. Xem danh sach khach hang");
            System.out.println("2. Nhap danh sach khanh hang");
            System.out.println("3. Them tai khoan ATM");
            System.out.println("4. Chuyen tien");
            System.out.println("5. Rut tien");
            System.out.println("6. Tra cuu lich su giao dich");
            System.out.println("0. Thoat");
            System.out.println("+----------+----------------------+-----------+");
            System.out.println("Chuc nang: ");
            int choose = Integer.parseInt(scanner.nextLine());
            do {
                switch (choose) {
                    case 1:
                        activeBank.showCustomers();
                        initUI();
                        break;
                    case 2:
                        enterCustomerlist();
                        initUI();
                        break;
                    case 3:
                        addSavingAccount();
                        initUI();
                        break;
                    case 4:
                        transferMoney();
                        initUI();
                        break;
                    case 5:
                        withdraw();
                        initUI();
                        break;
                    case 6:
                        //  historyTransaction();
                        break;
                    case 0:
                        System.out.println("Cam on ban da su dung dich vu cua chung toi!");
                        System.exit(choose);
                        break;
                    default:
                        System.out.println("Chuc nang khong ton tai! Hay chon lai ma chuc nang");
                        initUI();
                        break;
                }
            } while (runAgain);
        } catch (Exception e) {
            e.printStackTrace();
            initUI();
        }
    }

    //Nhập danh sách khách hàng
    private static void enterCustomerlist() throws IOException {
        System.out.println("Nhập đường dẫn đến tệp");
        String pathString = scanner.nextLine();
        activeBank.addCustomers(pathString);
    }

    //Thêm tài khoản ATM
    public static void addSavingAccount() throws IOException {
        System.out.print("Nhập mã số của khách hàng :");
        String customerID = scanner.nextLine();
        activeBank.addSavingAccount(scanner, customerID);
    }

    //Chuyển tiền
    public static void transferMoney() throws Exception {
        System.out.print("Nhập mã số của khách hàng :");
        String customerID = scanner.nextLine();
        boolean customerFound = false; // Biến để kiểm tra xem khách hàng có được tìm thấy không
        List<Customer> customers = CustomersDao.list();
        for (Customer customer : customers) {
            List<Account> accounts = AccountDao.list();
            accounts.stream()
                    .filter(account -> customer.getCustomerId().equals(account.getCustomerID()))
                    .forEach(customer::addAccount);
            if (customer.getCustomerId().equals(customerID)) {
                customer.disPlayInformation();
                customerFound = true;//đánh dấu khách hàng đã tìm thấy
                customer.tranfers(scanner,customerID);
                break;//dừng vòng lặp khi tìm thấy khách hàng
            }
        }
        if (!customerFound) {
            System.out.println("Không tìm thấy khách hàng");
        }
    }
//            if (customer.getCustomerId().equals(customerID)){
//                customer.disPlayInformation();
//                System.out.println(accounts);
//             //   customer.tranfers(scanner);
//            }else {
//                System.out.println("Khách hàng không tồn tại");
//            }


private static void withdraw() throws IOException {
    System.out.print("Nhập mã số khách hàng :");
    String customerID = scanner.nextLine();
    List<Customer> customers = CustomersDao.list();
    for (Customer customer : customers) {
        List<Account> accounts = AccountDao.list();
        accounts.stream()
                .filter(account -> customer.getCustomerId().equals(account.getCustomerID()))
                .forEach(customer::addAccount);
        if (customer.getCustomerId().equals(customerID)) {
            customer.disPlayInformation();
            customer.withdraw(scanner);
        } else {
            System.out.println("Khách hàng không tồn tại");
        }
    }
}

private static String accountNumberFromUser() {
    System.out.println("Vui lòng nhập số tài khoản");
    String stk = scanner.nextLine();
    while (stk.length() != 6) {
        System.out.println("Số tài khoản phải gồm 6 chứ số");
        System.out.println("Vui lòng nhập lại");
        System.out.println("Hoac chon no de thoat");
        stk = scanner.nextLine();
        if (stk.equalsIgnoreCase("No")) {
            System.out.println("Bạn đã thoát");
            initUI();
            break;
        }
    }
    return stk;
}

private static double balanceFromUser() {
    while (true) {
        System.out.println("Nhap so tien ");
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

}
