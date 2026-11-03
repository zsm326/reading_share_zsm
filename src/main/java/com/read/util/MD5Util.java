package com.read.util;

import java.security.MessageDigest;

/**
 * MD5加密工具类：用户密码加密存储
 */
public class MD5Util {

    /**
     * 对字符串进行MD5加密，返回32位大写十六进制字符串
     */
    public static String encrypt(String text) {
        if (text == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(text.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02X", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }
}
