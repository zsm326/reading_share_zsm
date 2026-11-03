package com.read.dao;

import com.read.entity.Book;
import com.read.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 书籍数据访问层（JDBC）
 */
public class BookDao {

    /** 录入书籍（含提交人、书籍审核状态、封面审核信息） */
    public int insert(Book book) {
        String sql = "INSERT INTO book(name,author,isbn,cover,intro,cover_status,cover_user_id,cover_time,"
                + "submit_user_id,book_status,user_id) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, book.getName());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, book.getCover());
            ps.setString(5, book.getIntro());
            ps.setString(6, book.getCoverStatus() == null ? "pending" : book.getCoverStatus());
            if (book.getCoverUserId() == null) {
                ps.setNull(7, java.sql.Types.INTEGER);
            } else {
                ps.setInt(7, book.getCoverUserId());
            }
            ps.setTimestamp(8, book.getCoverTime() == null
                    ? null : new java.sql.Timestamp(book.getCoverTime().getTime()));
            if (book.getSubmitUserId() == null) {
                ps.setNull(9, java.sql.Types.INTEGER);
            } else {
                ps.setInt(9, book.getSubmitUserId());
            }
            ps.setString(10, book.getBookStatus() == null ? "pending" : book.getBookStatus());
            if (book.getUserId() == null) {
                ps.setNull(11, java.sql.Types.INTEGER);
            } else {
                ps.setInt(11, book.getUserId());
            }
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 按id查询书籍 */
    public Book findById(int id) {
        String sql = "SELECT * FROM book WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return buildBook(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return null;
    }

    /**
     * 书籍搜索（前台展示用）：只查审核通过（book_status='pass'）的书籍，
     * pending / reject 不对外展示
     */
    public List<Book> search(String keyword) {
        return queryBooks(keyword, true);
    }

    /** 书籍搜索（管理员后台用）：不过滤审核状态，返回全部书籍 */
    public List<Book> searchAll(String keyword) {
        return queryBooks(keyword, false);
    }

    /** 通用书籍查询；onlyPass=true 时仅返回审核通过的书籍 */
    private List<Book> queryBooks(String keyword, boolean onlyPass) {
        List<Book> list = new ArrayList<>();
        boolean hasKey = keyword != null && !keyword.trim().isEmpty();
        String passCond = onlyPass ? "book_status = 'pass'" : "1=1";
        String sql = hasKey
                ? "SELECT * FROM book WHERE " + passCond + " AND (name LIKE ? OR author LIKE ?) ORDER BY id DESC"
                : "SELECT * FROM book WHERE " + passCond + " ORDER BY id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            if (hasKey) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(1, like);
                ps.setString(2, like);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(buildBook(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return list;
    }

    /**
     * 更新书籍封面（上传成功后调用）
     * @param id          书籍id
     * @param cover       图片文件名
     * @param coverStatus 审核状态 pending/pass
     * @param coverUserId 上传人id
     */
    public int updateCover(int id, String cover, String coverStatus, int coverUserId) {
        String sql = "UPDATE book SET cover=?, cover_status=?, cover_user_id=?, cover_time=NOW() WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, cover);
            ps.setString(2, coverStatus);
            ps.setInt(3, coverUserId);
            ps.setInt(4, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 管理员审核：修改封面状态 pass / reject */
    public int updateCoverStatus(int id, String coverStatus) {
        String sql = "UPDATE book SET cover_status=? WHERE id=?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, coverStatus);
            ps.setInt(2, id);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 待审核封面列表（联表查上传人昵称），供管理员后台审核 */
    public List<Book> findPendingCovers() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT b.*, u.nickname AS cover_user_name FROM book b "
                + "LEFT JOIN `user` u ON b.cover_user_id = u.id "
                + "WHERE b.cover_status = 'pending' AND b.cover IS NOT NULL AND b.cover <> '' "
                + "ORDER BY b.cover_time DESC, b.id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Book b = buildBook(rs);
                b.setCoverUserName(rs.getString("cover_user_name"));
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return list;
    }

    /** 待审核书籍列表（联表查提交人昵称），供管理员后台审核 */
    public List<Book> findPendingBooks() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT b.*, u.nickname AS submit_user_name FROM book b "
                + "LEFT JOIN `user` u ON b.submit_user_id = u.id "
                + "WHERE b.book_status = 'pending' "
                + "ORDER BY b.id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Book b = buildBook(rs);
                b.setSubmitUserName(rs.getString("submit_user_name"));
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return list;
    }

    /** 审核通过：书籍公开展示，提交人成为该书分享者（user_id = submit_user_id） */
    public int approveBook(int id) {
        String sql = "UPDATE book SET book_status='pass', user_id=submit_user_id WHERE id=?";
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

    /** 驳回：书籍不对外展示 */
    public int rejectBook(int id) {
        String sql = "UPDATE book SET book_status='reject' WHERE id=?";
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

    /** 删除书籍（管理员用，需先清理关联数据） */
    public int delete(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);
            // 先删除引用该书籍的书单关联和笔记（笔记的评论由外键级联删除）
            ps = conn.prepareStatement("DELETE FROM list_book WHERE book_id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            ps = conn.prepareStatement("DELETE FROM note WHERE book_id = ?");
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
            ps = conn.prepareStatement("DELETE FROM book WHERE id = ?");
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            conn.commit();
            return rows;
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (Exception ignored) {}
            }
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** ResultSet -> Book 对象 */
    private Book buildBook(ResultSet rs) throws Exception {
        Book b = new Book();
        b.setId(rs.getInt("id"));
        b.setName(rs.getString("name"));
        b.setAuthor(rs.getString("author"));
        b.setIsbn(rs.getString("isbn"));
        b.setCover(rs.getString("cover"));
        b.setIntro(rs.getString("intro"));
        b.setCoverStatus(rs.getString("cover_status"));
        Object coverUserId = rs.getObject("cover_user_id");
        b.setCoverUserId(coverUserId == null ? null : ((Number) coverUserId).intValue());
        b.setCoverTime(rs.getTimestamp("cover_time"));
        Object userId = rs.getObject("user_id");
        b.setUserId(userId == null ? null : ((Number) userId).intValue());
        Object submitUserId = rs.getObject("submit_user_id");
        b.setSubmitUserId(submitUserId == null ? null : ((Number) submitUserId).intValue());
        b.setBookStatus(rs.getString("book_status"));
        b.setCreateTime(rs.getTimestamp("create_time"));
        return b;
    }
}
