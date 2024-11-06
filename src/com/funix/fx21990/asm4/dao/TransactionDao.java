package com.funix.fx21990.asm4.dao;

import com.funix.fx21990.asm4.model.Transation;
import com.funix.fx21990.asm4.service.BinaryFileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransactionDao {
    private final static String FILE_PATH = "store/transactions.dat";

    public static void save(List<Transation> transations) throws IOException {
        BinaryFileService.writeFile(FILE_PATH, transations);
    }

    public static List<Transation> list(){
        return BinaryFileService.readFile(FILE_PATH);
    }

    public static void update(Transation editTransaction) throws IOException {
        List<Transation> transations = list();
        boolean hasExit = transations.stream().anyMatch(account -> account.getAccountNumber().equals(editTransaction.getAccountNumber()));

        List<Transation> updateTransaction;
        if(!hasExit){
            updateTransaction = new ArrayList<>(transations);
            updateTransaction.add(editTransaction);
        }else{
            updateTransaction = new ArrayList<>();
            for (Transation transation : transations){
                if (transation.getAccountNumber().equals(editTransaction.getAccountNumber())){
                    updateTransaction.add(editTransaction);
                }else {
                    updateTransaction.add(transation);
                }
            }
        }
        save(updateTransaction);
    }

}
