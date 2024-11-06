package com.funix.fx21990.asm4.test;

import com.funix.fx21990.asm4.dao.AccountDao;
import com.funix.fx21990.asm4.dao.TransactionDao;
import com.funix.fx21990.asm4.model.*;
import com.funix.fx21990.asm4.service.BinaryFileService;
import com.funix.fx21990.asm4.utils.Utils;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class unitTest {

    //kiểm tra 1 tệp nhị phân trống và xác minh danh sách trả về là rỗng
    @Test
    public void testReadFile_Empty() throws Exception {
        File tempFile = File.createTempFile("test_objects.dat", "store");
        List<Object> objects = BinaryFileService.readFile(tempFile.getAbsolutePath());
        assertEquals(0, objects.size());
        tempFile.delete();
    }

    //kiểm tra xác minh FileNotFoundException được đưa ra khi 1 tệp không tồn tại
    @Test
    public void testReadFile_FileNotFound() throws Exception {
        String nonExistenFile = "existen_file.dat";
        BinaryFileService.readFile(nonExistenFile);
    }

    //Kiểm tra ghi danh sách các đối tượng vào tệp có bằng nhau ko
    @Test
    public void testWriteFile_Success() throws Exception {
        Account account = new Account("0000000000001", 70000);
        Account account1 = new Account("0000000000002", 90000);
        List<Account> accounts = Arrays.asList(account1, account);
        String fileName = "account.dat";
        BinaryFileService.writeFile(fileName, accounts);

        List<Account> readAccount = BinaryFileService.readFile(fileName);
        assertEquals(accounts.size(), readAccount.size());
        for (int i = 0; i < accounts.size(); i++) {
            //kiểm tra xem số lượng phần tử trong hai danh sách có bằng nhau ko
            assertEquals(accounts.get(i).getAccountNumber(), readAccount.get(i).getAccountNumber());
            assertEquals(accounts.get(i).getBalance(), readAccount.get(i).getBalance());
        }
        File file = new File(fileName);
        file.delete();
    }

    //kiểm tra ghi và đọc tep có thành công ko
    @Test
    public void testWrite() throws Exception {
        Account account = new Account("000000000001", 10000000);
        List<Account> accounts = Arrays.asList(account);
        String filename = "account.dat";
        BinaryFileService.writeFile(filename, accounts);
        File file = new File(filename);
        assertTrue(file.exists());
        BinaryFileService.readFile(filename);
        File readFile = new File(filename);
        assertTrue(readFile.exists());
    }

    //test transfer
    //Khởi tạo dữ liệu
    private Account senderAccount;
    private Account receiverAccount;
    private List<Account> accountDao = AccountDao.list();

    @BeforeEach
    public void setUp() throws IOException {
        senderAccount = new SavingsAccount("123456", 9000000);
        receiverAccount = new SavingsAccount("123123", 700000);
        accountDao.add(senderAccount);
        accountDao.add(receiverAccount);
    }
    @Test
    public void testTransferSuccess() throws IOException {
        double transferAmount = 60000;
        if (senderAccount instanceof SavingsAccount) {
            ((SavingsAccount) senderAccount).transfer(receiverAccount, transferAmount);
            senderAccount.crateTransaction(transferAmount,Utils.getDateTime(),true, "SAVING");
            ((SavingsAccount) receiverAccount).deposit(receiverAccount, transferAmount);
        }
        System.out.println(senderAccount.getBalance());
        assertEquals(9000000 - transferAmount, senderAccount.getBalance());
    }

}
