package com.funix.fx21990.asm4.model;

import com.funix.fx21990.asm4.dao.AccountDao;
import com.funix.fx21990.asm4.iface.IReport;
import com.funix.fx21990.asm4.iface.ITrasfer;
import com.funix.fx21990.asm4.iface.ReportService;
import com.funix.fx21990.asm4.iface.Withdraw;
import com.funix.fx21990.asm4.utils.Utils;

import java.io.IOException;

public class SavingsAccount extends Account implements ReportService, Withdraw, ITrasfer{
    private static final long serialVersionUID = 6899794670413630308L; // Cập nhật giá trị này
    private static final double SAVINGS_ACCOUNT_MAX_WITHDRAW = 5000000;
    private static final double SAVINGS_ACCOUNT_MIN_WITHDRAW = 50000;

    public SavingsAccount(String accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void log(double amount) {
        System.out.println("+----------+--------------------+-----------+");
        System.out.println("        BIEN LAI GIAO DICH SAVINGS");
        System.out.printf("NGAY G/D:                   %s%n", Utils.getDateTime());
        System.out.printf("ATM ID:                     %s%n", "DIGITAL-BANK-ATM 2024");
        System.out.printf("SO TK:                      %s%n", getAccountNumber());
        System.out.printf("SO TIEN RUT:                    %s%n", Utils.getFormatMoney(amount));
        System.out.printf("SO DU:                      %s%n", Utils.getFormatMoney(getBalance()));
        System.out.println("+----------+--------------------+-----------+");
    }

    @Override
    public boolean withdraw(double amount) {
        if (isAccepted(amount)) {
            System.out.println("Rut tien thanh cong.");
            setBalance(getBalance() - amount);
//            addTransaction(new Transation(getAccountNumber(),amount,Utils.getDateTime()));
            log(amount);
            return true;
        }
        System.out.println("Rut tien that bai");
        return false;
    }

    @Override
    public boolean isAccepted(double amount) {
        if (amount >= SAVINGS_ACCOUNT_MIN_WITHDRAW && amount % 10000 == 0
                && (getBalance() - amount) >= SAVINGS_ACCOUNT_MIN_WITHDRAW) {
            if (!isPremium()) {
                return amount <= SAVINGS_ACCOUNT_MAX_WITHDRAW;
            } else {
                return true;
            }
        }
        System.out.println("So tien khong du de thuc hien giao dich");
        return false;
    }

    @Override
    public void transfer(Account receiveAccount, double amount) throws IOException {
        if (isAccepted(amount)) {
            if (receiveAccount.getType() == "DEPOSIT"){
                receiveAccount.setBalance(getBalance() + amount);
                AccountDao.update(receiveAccount);
                System.out.println("Chuyển tiền thành công, biên lại giao dịch");

            }if (receiveAccount.getType() == "TRANSFERS"){
                System.out.println("Chuyển tiền thành công, biên lại giao dịch");
                receiveAccount.setBalance(getBalance() - amount);
                AccountDao.update(receiveAccount);
            }
        }
    }

}
