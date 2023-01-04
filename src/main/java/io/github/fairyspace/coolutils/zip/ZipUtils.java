package io.github.fairyspace.coolutils.zip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.concurrent.Executors.*;

public class ZipUtils {

    public static void toZip(String sourceDirectory, OutputStream out) throws IOException {
        long start = System.currentTimeMillis();
        File sourceFile = new File(sourceDirectory);
        ZipOutputStream zos=new ZipOutputStream(out);
        /*判断传入的是不是存在*/
        if (sourceFile.exists()) {
            /*获取所有文件*/
            compress(sourceFile,zos,sourceFile.getName(),false);
            out.close();
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) +" ms");
        }

    }


    public static void toZip(List<File> inputFiles, OutputStream out) throws IOException {
        long start = System.currentTimeMillis();

        ZipOutputStream zos=new ZipOutputStream(out);
        /*判断传入的是不是存在*/

        for (File inputFile : inputFiles) {
            if (inputFile.exists()) {
                /*获取所有文件*/
                compress(inputFile,zos,inputFile.getName(),false);
                long end = System.currentTimeMillis();
                System.out.println("压缩完成，耗时：" + (end - start) +" ms");
            }
        }
        out.close();
    }





    private static void compress(File sourceFile, ZipOutputStream zos,String name, boolean KeepDirStructure) throws IOException {
        /*是目录结构*/
        if (sourceFile.isDirectory()) {
            File[] files = sourceFile.listFiles();
            /*保持内含目录*/
            String prefix="";

            if(KeepDirStructure){
                prefix = name + "/";
                if (files.length==0) {
                    /*空目录*/
                    zos.putNextEntry(new ZipEntry(prefix));
                    zos.closeEntry();
                }
            }
            for (File file : files) {
                compress(file,zos,prefix+file.getName(),KeepDirStructure);
            }
        }else{
            /*非目录结构*/
            zos.putNextEntry(new ZipEntry(name));
            InputStream inputStream = new FileInputStream(sourceFile);
            byte[] buff = new byte[2048];
            int len=0;
            while ((len = inputStream.read(buff) )!= -1) {
                zos.write(buff,0,len);
            }
            zos.closeEntry();
            inputStream.close();
        }

    }
}
