package com.read.util;

import com.read.entity.Book;
import com.read.entity.User;

/**
 * 封面可见性工具类：根据 cover_status 判断当前用户能否看到封面
 *
 * 展示规则：
 * 1. pass     —— 所有人可见
 * 2. pending  —— 仅上传者本人、管理员可见
 * 3. reject   —— 所有人不可见
 */
public class CoverUtil {

    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_PASS = "pass";
    public static final String STATUS_REJECT = "reject";

    /** 当前用户能否看到这本书的封面 */
    public static boolean canView(Book book, User viewer) {
        if (book == null || book.getCover() == null || book.getCover().trim().isEmpty()) {
            return false;
        }
        String status = book.getCoverStatus();
        if (STATUS_PASS.equals(status)) {
            return true;
        }
        if (STATUS_PENDING.equals(status)) {
            if (viewer == null) {
                return false;                       // 游客
            }
            if (viewer.getRole() != null && viewer.getRole() == 1) {
                return true;                        // 管理员
            }
            return book.getCoverUserId() != null
                    && book.getCoverUserId().equals(viewer.getId());   // 上传者本人
        }
        return false;   // reject 或状态异常
    }

    /** 不可见时清空 cover 字段，页面统一显示“暂无封面” */
    public static void mask(Book book, User viewer) {
        if (book != null && !canView(book, viewer)) {
            book.setCover(null);
        }
    }
}
