package io.github.fairyspace.coolutils.image;

import java.io.*;

/**
 * ????????????�����ҳ���������????????????
 * ? Program: coolutils
 * ? Description:
 * ? @author: xuquanru
 * ? Create: 2022/12/8
 * ????????????�ж���ꡣ�δ������????????????
 **/
public class FileUtil {

    public static byte[] file2byte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
