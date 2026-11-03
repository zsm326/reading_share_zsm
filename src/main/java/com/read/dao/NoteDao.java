package com.read.dao;

import com.read.entity.Note;
import com.read.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 读书笔记数据访问层（JDBC）
 */
public class NoteDao {

    /** 基础查询列：关联用户昵称和书籍名称 */
    private static final String BASE_SQL =
            "SELECT n.*, u.nickname, b.name AS book_name FROM note n " +
            "LEFT JOIN user u ON n.user_id = u.id " +
            "LEFT JOIN book b ON n.book_id = b.id ";

    /** 发布笔记 */
    public int insert(Note note) {
        String sql = "INSERT INTO note(user_id,book_id,title,content) VALUES(?,?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, note.getUserId());
            ps.setInt(2, note.getBookId());
            ps.setString(3, note.getTitle());
            ps.setString(4, note.getContent());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 修改笔记 */
    public int update(Note note) {
        String sql = "UPDATE note SET title = ?, content = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, note.getTitle());
            ps.setString(2, note.getContent());
            ps.setInt(3, note.getId());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 删除笔记（评论由外键级联删除） */
    public int delete(int id) {
        String sql = "DELETE FROM note WHERE id = ?";
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

    /** 按id查询笔记详情 */
    public Note findById(int id) {
        String sql = BASE_SQL + "WHERE n.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return buildNote(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return null;
    }

    /** 查询某用户的全部笔记（我的笔记） */
    public List<Note> findByUserId(int userId) {
        return queryList(BASE_SQL + "WHERE n.user_id = ? ORDER BY n.id DESC", userId);
    }

    /** 查询某书籍下的全部笔记 */
    public List<Note> findByBookId(int bookId) {
        return queryList(BASE_SQL + "WHERE n.book_id = ? ORDER BY n.id DESC", bookId);
    }

    /**
     * 笔记搜索：keyword 为空查全部，否则按标题/内容模糊匹配
     */
    public List<Note> search(String keyword) {
        boolean hasKey = keyword != null && !keyword.trim().isEmpty();
        if (!hasKey) {
            return queryList(BASE_SQL + "ORDER BY n.id DESC", null);
        }
        String sql = BASE_SQL + "WHERE n.title LIKE ? OR n.content LIKE ? ORDER BY n.id DESC";
        List<Note> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            String like = "%" + keyword.trim() + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(buildNote(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return list;
    }

    /** 通用单参数查询 */
    private List<Note> queryList(String sql, Integer param) {
        List<Note> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            if (param != null) {
                ps.setInt(1, param);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(buildNote(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return list;
    }

    /** ResultSet -> Note 对象（含 nickname / bookName 扩展字段） */
    private Note buildNote(ResultSet rs) throws Exception {
        Note n = new Note();
        n.setId(rs.getInt("id"));
        n.setUserId(rs.getInt("user_id"));
        n.setBookId(rs.getInt("book_id"));
        n.setTitle(rs.getString("title"));
        n.setContent(rs.getString("content"));
        n.setCreateTime(rs.getTimestamp("create_time"));
        n.setUpdateTime(rs.getTimestamp("update_time"));
        n.setNickname(rs.getString("nickname"));
        n.setBookName(rs.getString("book_name"));
        return n;
    }
}
