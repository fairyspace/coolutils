package io.github.fairyspace.coolutils;

import io.github.fairyspace.coolutils.color.ColorUtils;
import io.github.fairyspace.coolutils.file.FileUtil;
import io.github.fairyspace.coolutils.zip.ZipUtil;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TestMain {
    public static void main(String[] args) throws Exception {
      /*  String spec1 = "https://cdn.midjourney.com/2c9c653d-f8ca-4ed1-8a92-656cc3770d1e/grid_0_128_N.webp";
        URL url1 = new URL(spec1);
        String spec2 = "https://cdn.midjourney.com/4690fc2f-8e5f-4b8e-b328-9dfac8ab9c1a/grid_0_128_N.webp";
        URL url2 = new URL(spec2);

        File file1=new File("C:\\Users\\xuquanru\\Desktop\\test\\test1.txt");

        List<Object> zipfiles = new ArrayList<>();
        zipfiles.add(url1);
        zipfiles.add(url2);
        zipfiles.add(file1);
        ZipUtil.toZip(zipfiles, "C:\\Users\\xuquanru\\Desktop\\test\\hello.zip");*/
        ZipUtil.unZip("C:\\Users\\xuquanru\\Desktop\\test\\hello.zip","C:\\Users\\xuquanru\\Desktop\\out\\");
      // FileUtil.url2File(spec1,null,null);
    }

}
