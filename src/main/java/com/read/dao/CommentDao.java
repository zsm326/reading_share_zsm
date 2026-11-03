package com.read.dao;

import com.read.entity.Comment;
import com.read.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论数据访问层（JDBC）
 */
public class CommentDao {

    /** 发表评论 */
    public int insert(Comment comment) {
        String sql = "INSERT INTO comment(user_id,note_id,content) VALUES(?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, comment.getUserId());
            ps.setInt(2, comment.getNoteId());
            ps.setString(3, comment.getContent());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 删除评论 */
    public int delete(int id) {
        String sql = "DELETE FROM comment WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 按id查询评论（删除前校验所有者用） */
    public Comment findById(int id) {
        String sql = "SELECT * FROM comment WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Comment c = new Comment();
                c.setId(rs.getInt("id"));
                c.setUserId(rs.getInt("user_id"));
                c.setNoteId(rs.getInt("note_id"));
                c.setContent(rs.getString("content"));
                c.setCreateTime(rs.getTimestamp("create_time"));
                return c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return null;
    }

    /** 查询某笔记下的全部评论（联查评论者昵称） */
    public List<Comment> findByNoteId(int noteId) {
        List<Comment> list = new ArrayList<>();
        String sql = "SELECT c.*, u.nickname FROM comment c " +
                "LEFT JOIN user u ON c.user_id = u.id " +
                "WHERE c.note_id = ? ORDER BY c.id";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, noteId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(buildComment(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return list;
    }

    /** 查询全部评论（管理员后台用，联查昵称和笔记标题） */
    public List<Comment> findAll() {
        List<Comment> list = new ArrayList<>();
        String sql = "SELECT c.*, u.nickname, n.title AS note_title FROM comment c " +
                "LEFT JOIN user u ON c.user_id = u.id " +
                "LEFT JOIN note n ON c.note_id = n.id " +
                "ORDER BY c.id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Comment c = buildComment(rs);
                c.setNoteTitle(rs.getString("note_title"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return list;
    }

    /** ResultSet -> Comment 对象 */
    private Comment buildComment(ResultSet rs) throws Exception {
        Comment c = new Comment();
        c.setId(rs.getInt("id"));
        c.setUserId(rs.getInt("user_id"));
        c.setNoteId(rs.getInt("note_id"));
        c.setContent(rs.getString("content"));
        c.setCreateTime(rs.getTimestamp("create_time"));
        c.setNickname(rs.getString("nickname"));
        return c;
    }
}
