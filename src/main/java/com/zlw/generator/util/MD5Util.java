package com.zlw.generator.util;

import java.security.MessageDigest;

/**
 * @Description
 * @Author zhangliewei
 * @Date 2016/7/6
 *
 */
public class MD5Util {
    public MD5Util() {
    }

    public static String MD5(String source, int times) {
        String s = null;
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest e = MessageDigest.getInstance("MD5");
            e.update(source.getBytes("utf-8"));
            byte[] tmp = e.digest();

            for(int str = 0; str < times - 1; ++str) {
                tmp = e.digest(tmp);
            }

            char[] var11 = new char[32];
            int k = 0;

            for(int i = 0; i < 16; ++i) {
                byte byte0 = tmp[i];
                var11[k++] = hexDigits[byte0 >>> 4 & 15];
                var11[k++] = hexDigits[byte0 & 15];
            }

            s = new String(var11);
        } catch (Exception var10) {
            var10.printStackTrace();
        }

        return s.toUpperCase();
    }

    public static String MD5(String source) {
        return MD5(source, 1);
    }
}
