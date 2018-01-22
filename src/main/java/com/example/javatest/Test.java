package com.example.javatest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.SequenceInputStream;
import java.util.Enumeration;
import java.util.Vector;

/**
 */
public class Test {

    public static void main(String[] args) {
        division(new File("f://影视/28 李开复 成长中的十个启发.bhd"), 30 * 1024 * 1024);
        merge();
    }

    /**
     * 文件分割
     */
    private static void division(File targetFile, long cutSize) {
        // 如果目标文件为null直接返回
        if (targetFile == null) return;
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(targetFile));
            BufferedOutputStream out = null;
            // count：文件分割的总个数
            int count = targetFile.length() % cutSize == 0 ? (int) (targetFile.length() / cutSize) :
                    (int) (targetFile.length() / cutSize + 1);
            byte[] buffer = null;
            int len = -1;

            for (int i = 1; i <= count; i++) {
                out = new BufferedOutputStream(new FileOutputStream("f://" + i +
                        "-temp-" + targetFile.getName()));
                // time:每一份分割文件需要读写的次数
                int time = 0;
                if (cutSize < 1024) {
                    buffer = new byte[(int) cutSize];
                    time = 1;
                } else {
                    buffer = new byte[1024];
                    time = (int) (cutSize / buffer.length);
                }
                // 写整数部分
                while (time > 0 && (len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                    // 这里不需要频繁写入文件，一次读1k，30M放内存，最后关流的时候再一次性写入文件中
                    // 当然，如果过大的话需要频繁写入
//                    out.flush();
                    time--;
                }
                // 写余数部分
                if (cutSize % 1024 != 0) {
                    buffer = new byte[(int) cutSize % 1024];
                    len = in.read(buffer);
                    out.write(buffer, 0, len);
                }
                out.close();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 文件合并
     */
    private static void merge() {
        try {
            // 1.5个输入源
            BufferedInputStream in1 = new BufferedInputStream(new FileInputStream("f://1-temp-28 李开复 成长中的十个启发.bhd"));
            BufferedInputStream in2 = new BufferedInputStream(new FileInputStream("f://2-temp-28 李开复 成长中的十个启发.bhd"));
            BufferedInputStream in3 = new BufferedInputStream(new FileInputStream("f://3-temp-28 李开复 成长中的十个启发.bhd"));
            BufferedInputStream in4 = new BufferedInputStream(new FileInputStream("f://4-temp-28 李开复 成长中的十个启发.bhd"));
            BufferedInputStream in5 = new BufferedInputStream(new FileInputStream("f://5-temp-28 李开复 成长中的十个启发.bhd"));
            BufferedInputStream in6 = new BufferedInputStream(new FileInputStream("f://6-temp-28 李开复 成长中的十个启发.bhd"));

            // 2.添加到集合
            Vector<BufferedInputStream> vector = new Vector<>();
            vector.add(in1);
            vector.add(in2);
            vector.add(in3);
            vector.add(in4);
            vector.add(in5);
            vector.add(in6);
            Enumeration<BufferedInputStream> enumeration = vector.elements();

            // 3.创建合并流对象
            SequenceInputStream sis = new SequenceInputStream(enumeration);

            // 4.合并文件
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("f://28 李开复 成长中的十个启发.bhd"));
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = sis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bos.close();
            sis.close();
            System.out.println("合并完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

