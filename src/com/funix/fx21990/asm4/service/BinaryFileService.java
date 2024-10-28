package com.funix.fx21990.asm4.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryFileService {

    public BinaryFileService() {
    }

    public static <T> List<T> readFile(String fileName) {
        List<T> objects = new ArrayList<>();

        // Đọc file nhị phân
        try (ObjectInputStream file = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)))) {
            while (true) {
                try {
                    T object = (T) file.readObject();
                    objects.add(object);
                } catch (EOFException e) {
                    break; // Kết thúc khi gặp EOF
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("File không tìm thấy: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Lỗi I/O: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy lớp: " + e.getMessage());
        }
        return objects;
    }

    // Nhận đầu vào là đường dẫn đến thư mục và danh sách đối tượng cần ghi
    public static <T> void writeFile(String fileName, List<T> objects) {
        try (ObjectOutputStream file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)))) {
            for (T object : objects) {
                file.writeObject(object);
            }
            file.close();
        } catch (IOException e) {
            System.out.println("Lỗi ghi vào file: " + e.getMessage());
        }
    }
}