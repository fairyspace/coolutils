package io.github.fairyspace.coolutils.image;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.net.URL;
import java.util.Iterator;

/**
 * @author xuquanru
 */
public class ImageUtil {
    /*base64转图片*/
    public static byte[] base64StrToImage(String imgStr, String path) {
        if (imgStr == null) {
            return null;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            //处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            //文件夹不存在则自动创建
            File tempFile = new File(path);
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(tempFile);
            out.write(b);
            out.flush();
            out.close();
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 图片转base64字符串
     *
     * @param imgFile 图片路径
     * @return
     */
    public static String imageToBase64Str(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);
    }

    /*把网络图片转base64*/
    public static String urlImgageToBase64(String remark) {
        ByteArrayOutputStream outputStream = null;
        try {
            URL url = new URL(remark);
            BufferedImage bufferedImage = ImageIO.read(url);
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BASE64Encoder encoder = new BASE64Encoder();
        String baseStr = encoder.encode(outputStream.toByteArray());
        return baseStr;

    }



    public static byte[] compressPicByQuality(byte[] imgByte, float quality) {
        byte[] imgBytes = null;
        try {
            ByteArrayInputStream byteInput = new ByteArrayInputStream(imgByte);
            BufferedImage image = ImageIO.read(byteInput);

            if (image == null) {
                return null;
            }

            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");

            ImageWriter writer = (ImageWriter) iter.next();

            ImageWriteParam iwp = writer.getDefaultWriteParam();

            iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);

            iwp.setCompressionQuality(quality);
            iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
            ColorModel colorModel = ColorModel.getRGBdefault();

            iwp.setDestinationType(
                    new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            IIOImage iIamge = new IIOImage(image, null, null);


            writer.setOutput(ImageIO.createImageOutputStream(byteArrayOutputStream));
            writer.write(null, iIamge, iwp);
            imgBytes = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            System.out.println("write errro");
            e.printStackTrace();
        }
        return imgBytes;
    }


}
