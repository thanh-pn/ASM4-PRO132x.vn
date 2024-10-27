package com.funix.fx21990.asm4.dao;

import com.funix.fx21990.asm4.model.Customer;
import com.funix.fx21990.asm4.service.BinaryFileService;

import java.io.IOException;
import java.util.List;

//customerDao định nghĩa lớp trung gian thao tác với file để lấy dữ liệu và thêm mới, update
public class CustomersDao {
    private final static String FILE_PATH = "store/customer.dat";

    public static void save(List<Customer> customers) throws IOException {
        BinaryFileService.writeFile(FILE_PATH, customers);
    }

    public static List<Customer> list(){
        return BinaryFileService.readFile(FILE_PATH);
    }

    public Customer getCustomerById(String customerId) {
        List<Customer> customers = list();
        for (Customer customer : customers) {
            if (customer.getCustomerId().equals(customerId)) {
                return customer;
            }
        }
        return null;
    }

}
