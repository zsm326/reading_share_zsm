package com.read.dao;

import com.read.entity.Collect;
import com.read.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏数据访问层（JDBC）
 * targetType：1=书单 2=笔记
 */
public class CollectDao {

    /** 添加收藏 */
    public int insert(int userId, int targetType, int targetId) {
        String sql = "INSERT INTO collect(user_id,target_type,target_id) VALUES(?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, targetType);
            ps.setInt(3, targetId);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 取消收藏 */
    public int delete(int userId, int targetType, int targetId) {
        String sql = "DELETE FROM collect WHERE user_id = ? AND target_type = ? AND target_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, targetType);
            ps.setInt(3, targetId);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 判断用户是否已收藏某目标 */
    public boolean exists(int userId, int targetType, int targetId) {
        String sql = "SELECT COUNT(*) FROM collect WHERE user_id = ? AND target_type = ? AND target_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, targetType);
            ps.setInt(3, targetId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return false;
    }

    /** 统计某目标被收藏的次数 */
    public int countByTarget(int targetType, int targetId) {
        String sql = "SELECT COUNT(*) FROM collect WHERE target_type = ? AND target_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, targetType);
            ps.setInt(2, targetId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return 0;
    }

    /**
     * 查询用户的收藏列表（联查目标名称）
     * @param targetType 1=书单 2=笔记
     */
    public List<Collect> findByUser(int userId, int targetType) {
        List<Collect> list = new ArrayList<>();
        // 书单名取 book_list.name，笔记名取 note.title
        String joinTable = targetType == 1 ? "book_list" : "note";
        String nameColumn = targetType == 1 ? "name" : "title";
        String sql = "SELECT c.*, t." + nameColumn + " AS target_name FROM collect c " +
                "LEFT JOIN " + joinTable + " t ON c.target_id = t.id " +
                "WHERE c.user_id = ? AND c.target_type = ? ORDER BY c.id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, targetType);
            rs = ps.executeQuery();
            while (rs.next()) {
                Collect c = new Collect();
                c.setId(rs.getInt("id"));
                c.setUserId(rs.getInt("user_id"));
                c.setTargetType(rs.getInt("target_type"));
                c.setTargetId(rs.getInt("target_id"));
                c.setCreateTime(rs.getTimestamp("create_time"));
                c.setTargetName(rs.getString("target_name"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return list;
    }
}
