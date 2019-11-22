package cn.itcast.utils;

import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class HandleImgUtils {

    public static boolean handleImg(String imgString, String path) {
        String im = processImgStr(imgString);
        return generatorImage(im, path);
    }

    public static byte[] handleImg(String imgString) {
        String im = processImgStr(imgString);
        return generatorImage(im);
    }

    private static String processImgStr(String imgStr) {
        int headIndex = imgStr.indexOf(',') + 1;
        return imgStr.substring(headIndex);
    }


    private static boolean generatorImage(String imgStr, String filePath) {
        if (imgStr == null) {
            return false;
        } else {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                //解密过程
                byte[] imgByte = decoder.decodeBuffer(imgStr);
                //处理数据
                for (int i = 0; i < imgByte.length; i++) {
                    if (imgByte[i] < 0) {
                        imgByte[i] += 256;
                    }
                }
                OutputStream out = new FileOutputStream(filePath);
                //将图片存放入文件
                out.write(imgByte);
                out.flush();
                out.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private static byte[] generatorImage(String imgStr) {
        if (imgStr == null) {
            return null;
        } else {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                //解密过程
                byte[] imgByte = decoder.decodeBuffer(imgStr);
                //处理数据
                for (int i = 0; i < imgByte.length; i++) {
                    if (imgByte[i] < 0) {
                        imgByte[i] += 256;
                    }
                }
                return imgByte;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}

