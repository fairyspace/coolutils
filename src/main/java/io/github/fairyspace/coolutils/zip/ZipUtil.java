package io.github.fairyspace.coolutils.zip;

import com.sun.istack.internal.NotNull;
import io.github.fairyspace.coolutils.file.FileUtil;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.*;

public class ZipUtil {
    private static final int BUFFER = 1024;

    /**
     * 解压入口方法
     *
     * @param zipPath  zip文件物理地址
     * @param destFile 解压后存放路径
     * @throws Exception 异常
     */
    public static void unZip(String zipPath, String destFile) throws Exception {
        //解压缩执行方法
        long start = System.currentTimeMillis();
        decompressFile(new File(zipPath), new File(destFile));
        long end = System.currentTimeMillis();
        System.out.println("解压完成，耗时：" + (end - start) + " ms");
    }

    /**
     * 解压缩执行方法
     *
     * @param srcFile  压缩文件File实体
     * @param destFile 解压路径File实体
     * @throws Exception 异常
     */
    private static void decompressFile(File srcFile, File destFile) throws Exception {
        //创建数据输入流
        CheckedInputStream cis = new CheckedInputStream(new FileInputStream(srcFile), new CRC32());
        //创建压缩输入流
        ZipInputStream zis = new ZipInputStream(cis, StandardCharsets.UTF_8);
        //异常捕获的方式判断编码格式
        try {
            //判断代码，如果此代码未抛出异常，则表示编码为UTF-8
            decompressZis(zis, destFile);
        } catch (Exception e) {
            //如果乱码会抛异常，抛异常重新创建输入流，重新设置编码格式
            cis = new CheckedInputStream(new FileInputStream(srcFile), new CRC32());
            zis = new ZipInputStream(cis, Charset.forName("GBK"));
            //解压zip
            decompressZis(zis, destFile);
        }
        //关闭流
        zis.close();

    }

    /**
     * 文件 解压缩执行方法
     *
     * @param destFile 目标文件
     * @param zis      ZipInputStream
     * @throws Exception 异常
     */
    private static void decompressZis(ZipInputStream zis, File destFile) throws Exception {
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            //获取当前的ZIP条目路径
            String dir = destFile.getPath() + File.separator + entry.getName();
            File dirFile = new File(dir);
            //递归检查文件路径，路径上没有文件夹则创建，保证整条路径在本地存在
            fileProber(dirFile);
            //判断是否是文件夹
            if (entry.isDirectory()) {
                //如果是，创建文件夹
                dirFile.mkdirs();
                System.out.println(dirFile.getName());
            } else {
                //如果不是文件夹，数据流输出，生成文件
                decompressFile(dirFile, zis);
            }
            //关闭当前的ZIP条目并定位流
            zis.closeEntry();
        }
    }


    /**
     * 文件探针，当父目录不存在时，创建目录
     *
     * @param dirFile ZIP条目路径
     */
    public static void fileProber(File dirFile) {
        //获取此路径的父目录
        File parentFile = dirFile.getParentFile();
        //判断是否存在
        if (!parentFile.exists()) {
            // 递归寻找上级目录
            fileProber(parentFile);
            //直至存在，递归执行创建文件夹
            parentFile.mkdir();
        }
    }

    /**
     * 生成文件
     *
     * @param destFile 目标文件
     * @param zis      ZipInputStream
     * @throws Exception 异常
     */
    private static void decompressFile(File destFile, ZipInputStream zis) throws Exception {
        //创建输出流
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));
        //转成byte数组
        int count;
        byte[] data = new byte[BUFFER];
        //读取并写入文件
        while ((count = zis.read(data, 0, BUFFER)) != -1) {
            bos.write(data, 0, count);
        }
        //关闭数据流
        bos.close();
    }


    public static void toZip(String sourceDirectory, String destinationDirectory, @NotNull String zipFileName) throws IOException {
        toZip(sourceDirectory, destinationDirectory, zipFileName, false);
    }

    public static void toZip(String sourceDirectory, String destinationPath) throws IOException {
        toZip(sourceDirectory, destinationPath, false);
    }

    public static void toZip(String sourceDirectory, String destinationDirectory, @NotNull String zipFileName, Boolean KeepDirStructure) throws IOException {

        FileOutputStream out = makeFileOutputStream(destinationDirectory, zipFileName);
        long start = System.currentTimeMillis();
        File sourceFile = new File(sourceDirectory);
        ZipOutputStream zos = new ZipOutputStream(out);
        /*判断传入的是不是存在*/
        if (sourceFile.exists()) {
            /*获取所有文件*/
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            zos.finish();
            out.close();
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        }
    }

    public static void toZip(String sourceDirectory, String destinationPath, Boolean KeepDirStructure) throws IOException {
        FileOutputStream out = makeFileOutputStream(destinationPath);
        long start = System.currentTimeMillis();
        File sourceFile = new File(sourceDirectory);
        ZipOutputStream zos = new ZipOutputStream(out);
        /*判断传入的是不是存在*/
        if (sourceFile.exists()) {
            /*获取所有文件*/
            compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
            zos.finish();
            out.close();
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        }
    }


    public static void toZip(List<Object> urlOrFiles, String destinationPath) {
        long start = System.currentTimeMillis();
        List<InputStream> inputStreams = urlOrFiles.stream().map(urlOrFile -> {

            if (urlOrFile instanceof URL) {
                try {
                    URLConnection urlConnection = ((URL) urlOrFile).openConnection();
                    urlConnection.setRequestProperty("User-Agent", "Mozilla/4.76");
                    urlConnection.setConnectTimeout(50000);
                    urlConnection.setReadTimeout(50000);
                    InputStream inputStream = urlConnection.getInputStream();
                    return inputStream;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (urlOrFile instanceof File) {
                try {
                    return new FileInputStream(((File) urlOrFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        toZipByInputStreams(destinationPath, inputStreams);
        long end = System.currentTimeMillis();
        System.out.println("压缩完成，耗时：" + (end - start) + " ms");
    }


    private static void toZipByInputStreams(@NotNull String destinationPath, List<InputStream> inputStreams) {
        FileOutputStream out = makeFileOutputStream(destinationPath);
        ZipOutputStream zos = new ZipOutputStream(out);
        inputStreams.forEach(inputStream -> {
            try {
                byte[] bytes = FileUtil.inputStream2Bytes(inputStream);
                String mediaType = FileUtil.getMediaType(bytes);
                String fileName = FileUtil.createFileName(null, "." + mediaType);
                zos.putNextEntry(new ZipEntry(fileName));
                zos.write(bytes);
                zos.closeEntry();
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        try {
            zos.finish();
            zos.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void toZip(List<File> inputFiles, String destinationDirectory, @NotNull String zipFileName) throws IOException {
        FileOutputStream out = makeFileOutputStream(destinationDirectory, zipFileName);
        long start = System.currentTimeMillis();
        ZipOutputStream zos = new ZipOutputStream(out);
        /*判断传入的是不是存在*/
        for (File inputFile : inputFiles) {
            if (inputFile.exists()) {
                /*获取所有文件*/
                compress(inputFile, zos, inputFile.getName(), false);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        zos.finish();
        out.close();
    }


    private static FileOutputStream makeFileOutputStream(String destinationDirectory, @NotNull String zipFileName) {
        FileOutputStream out = null;
        try {
            if (!zipFileName.endsWith(".zip")) {
                zipFileName += ".zip";
            }
            if (destinationDirectory != null) {
                if (!destinationDirectory.endsWith(File.separator)) {
                    destinationDirectory = destinationDirectory + File.separator;
                }
                zipFileName = destinationDirectory + zipFileName;
            }
            out = new FileOutputStream(zipFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return out;
    }

    private static FileOutputStream makeFileOutputStream(String completedPath) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(completedPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return out;
    }


    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws IOException {
        /*是目录结构*/
        if (sourceFile.isDirectory()) {
            File[] files = sourceFile.listFiles();
            /*保持内含目录*/
            String prefix = "";
            if (KeepDirStructure) {
                prefix = name + "/";
                if (files.length == 0) {
                    /*空目录*/
                    zos.putNextEntry(new ZipEntry(prefix));
                    zos.closeEntry();
                }
            }
            for (File file : files) {
                compress(file, zos, prefix + file.getName(), KeepDirStructure);
            }
        } else {
            /*非目录结构*/
            zos.putNextEntry(new ZipEntry(name));
            InputStream inputStream = new FileInputStream(sourceFile);
            byte[] buff = new byte[BUFFER];
            int len = 0;
            while ((len = inputStream.read(buff)) != -1) {
                zos.write(buff, 0, len);
            }
            zos.closeEntry();
            inputStream.close();
        }

    }
}
