package com.read.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * 文件上传工具类：校验类型/大小并保存到指定目录
 */
public class UploadUtil {

    /** 允许的图片后缀 */
    private static final List<String> ALLOW_EXT = Arrays.asList("jpg", "jpeg", "png", "gif");
    /** 最大 2MB */
    private static final long MAX_SIZE = 2 * 1024 * 1024;

    /**
     * 保存上传的图片
     * @param file      Struts2 注入的上传文件
     * @param fileName  原始文件名
     * @param saveDir   保存目录（服务器真实路径）
     * @return 保存后的新文件名；校验失败返回 null；未上传返回空字符串 ""
     */
    public static String saveImage(File file, String fileName, String saveDir) {
        if (file == null || fileName == null || fileName.trim().isEmpty()) {
            return "";   // 用户未选择文件
        }
        String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOW_EXT.contains(ext)) {
            return null; // 类型不合法
        }
        if (file.length() > MAX_SIZE) {
            return null; // 超过大小限制
        }
        try {
            // UUID重命名，防止文件名冲突和中文乱码问题
            String newName = UUID.randomUUID().toString().replace("-", "") + "." + ext;
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            FileUtils.copyFile(file, new File(dir, newName));
            return newName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
