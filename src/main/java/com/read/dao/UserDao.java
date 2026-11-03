package com.read.dao;

import com.read.entity.User;
import com.read.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问层（JDBC）
 */
public class UserDao {

    /** 按用户名查询用户（登录校验用） */
    public User findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return buildUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return null;
    }

    /** 按id查询用户 */
    public User findById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return buildUser(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return null;
    }

    /** 注册新用户 */
    public int insert(User user) {
        String sql = "INSERT INTO user(username,password,nickname,role) VALUES(?,?,?,0)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getNickname());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 修改昵称 */
    public int updateNickname(int id, String nickname) {
        String sql = "UPDATE user SET nickname = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, nickname);
            ps.setInt(2, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 修改密码 */
    public int updatePassword(int id, String password) {
        String sql = "UPDATE user SET password = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, password);
            ps.setInt(2, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 更新头像 */
    public int updateAvatar(int id, String avatar) {
        String sql = "UPDATE user SET avatar = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, avatar);
            ps.setInt(2, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 查询全部用户（管理员后台用） */
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user ORDER BY id";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(buildUser(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return list;
    }

    /** ResultSet -> User 对象 */
    private User buildUser(ResultSet rs) throws Exception {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPassword(rs.getString("password"));
        u.setNickname(rs.getString("nickname"));
        u.setAvatar(rs.getString("avatar"));
        u.setRole(rs.getInt("role"));
        u.setCreateTime(rs.getTimestamp("create_time"));
        return u;
    }
}
