package com.funix.fx21990.asm4.service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BinaryFileService {
    public BinaryFileService() {
    }

    public static <T> List<T> readFile(String fileName) {
        List<T> objects = new ArrayList<>();

        //đọc file nhị phân
        try (ObjectInputStream file = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)))) {
            boolean eof = false;
            while (!eof) {
                try {
                    T object = (T) file.readObject();
                    objects.add(object);
                } catch (EOFException e) {
                    eof = true;
                }
            }
        } catch (EOFException e) {
            return new ArrayList<>();
        } catch (IOException io) {
            System.out.println("IO EOFException " + io.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException :" + e.getMessage());
        }
        return objects;
    }

    //nhận đâù vào là đường dẫn đến thư mục va danh sách đối tượng cần ghi
    public static <T> void writeFile(String fileName, List<T> objects) {
        try (ObjectOutputStream file = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)))) {
            for (T object : objects) {
                file.writeObject(object);
            }
        } catch (IOException e) {
            System.out.println("Lỗi ghi vào file" + e.getMessage());
        }
    }
}
