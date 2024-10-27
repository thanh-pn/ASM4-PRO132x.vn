package com.funix.fx21990.asm4.service;

import com.funix.fx21990.asm4.model.Customer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextFileService {
    //thao tác với file text, người dùng hiển thị và sữa đổi dữ liệu file text này
    private static final String COMMA_DELIMITER = ",";
//Phương thức để đọc tệp và trả về nội dung tệp dưới dạng danh sách các chuỗi mỗi giá trị được phân tách bằng dấu phẩy
    public static List<List<String>> readFile(String fileName) {
        List<List<String>> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                List<String> row = new ArrayList<>();
                for (String value : values) {
                    row.add(value);
                }
                dataList.add(row);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.err.println("Lỗi đọc file :" + e.getMessage());
        }
        return dataList;
    }
}
