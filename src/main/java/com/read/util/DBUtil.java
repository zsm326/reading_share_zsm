package com.read.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接工具类（原生JDBC）
 * 使用完务必调用 close() 释放资源
 */
public class DBUtil {

    // 数据库连接信息：优先读取 classpath 下的 db.properties，读取不到则使用以下默认值
    private static String URL =
            "jdbc:mysql://localhost:3306/reading_share?useUnicode=true&characterEncoding=utf8&useSSL=false";
    private static String USER = "root";
    private static String PASSWORD = "123456";

    static {
        // 加载 db.properties 中的数据库配置
        InputStream in = null;
        try {
            in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
            if (in != null) {
                Properties props = new Properties();
                props.load(in);
                URL = props.getProperty("jdbc.url", URL);
                USER = props.getProperty("jdbc.username", USER);
                PASSWORD = props.getProperty("jdbc.password", PASSWORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try { in.close(); } catch (Exception ignored) {}
            }
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL驱动加载失败", e);
        }
    }

    /** 获取数据库连接 */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /** 关闭资源（查询用） */
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException ignored) {}
        }
        close(conn, ps);
    }

    /** 关闭资源（增删改用） */
    public static void close(Connection conn, PreparedStatement ps) {
        if (ps != null) {
            try { ps.close(); } catch (SQLException ignored) {}
        }
        if (conn != null) {
            try { conn.close(); } catch (SQLException ignored) {}
        }
    }
}
