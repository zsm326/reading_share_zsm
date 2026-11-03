package com.read.dao;

import com.read.entity.Book;
import com.read.entity.BookList;
import com.read.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 书单数据访问层（JDBC）
 */
public class BookListDao {

    /** 创建书单 */
    public int insert(BookList bookList) {
        String sql = "INSERT INTO book_list(user_id,name,intro,is_public) VALUES(?,?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bookList.getUserId());
            ps.setString(2, bookList.getName());
            ps.setString(3, bookList.getIntro());
            ps.setInt(4, bookList.getIsPublic());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 修改书单 */
    public int update(BookList bookList) {
        String sql = "UPDATE book_list SET name = ?, intro = ?, is_public = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, bookList.getName());
            ps.setString(2, bookList.getIntro());
            ps.setInt(3, bookList.getIsPublic());
            ps.setInt(4, bookList.getId());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 删除书单（list_book 关联由外键级联删除） */
    public int delete(int id) {
        String sql = "DELETE FROM book_list WHERE id = ?";
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

    /** 按id查询书单 */
    public BookList findById(int id) {
        String sql = "SELECT l.*, u.nickname FROM book_list l " +
                "LEFT JOIN user u ON l.user_id = u.id WHERE l.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return buildBookList(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return null;
    }

    /** 查询某用户的全部书单（我的书单） */
    public List<BookList> findByUserId(int userId) {
        String sql = "SELECT l.*, u.nickname, " +
                "(SELECT COUNT(*) FROM list_book lb WHERE lb.list_id = l.id) AS book_count " +
                "FROM book_list l LEFT JOIN user u ON l.user_id = u.id " +
                "WHERE l.user_id = ? ORDER BY l.id DESC";
        return queryList(sql, userId);
    }

    /** 查询全部公开书单（首页/广场展示） */
    public List<BookList> findPublic() {
        String sql = "SELECT l.*, u.nickname, " +
                "(SELECT COUNT(*) FROM list_book lb WHERE lb.list_id = l.id) AS book_count " +
                "FROM book_list l LEFT JOIN user u ON l.user_id = u.id " +
                "WHERE l.is_public = 1 ORDER BY l.id DESC";
        return queryList(sql, null);
    }

    /** 通用书单查询，param 为 null 表示无参数 */
    private List<BookList> queryList(String sql, Integer param) {
        List<BookList> list = new ArrayList<>();
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
                list.add(buildBookList(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return list;
    }

    // ================= list_book 关联表操作 =================

    /** 向书单添加书籍 */
    public int addBook(int listId, int bookId) {
        String sql = "INSERT INTO list_book(list_id,book_id) VALUES(?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, listId);
            ps.setInt(2, bookId);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 从书单移除书籍 */
    public int removeBook(int listId, int bookId) {
        String sql = "DELETE FROM list_book WHERE list_id = ? AND book_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, listId);
            ps.setInt(2, bookId);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps);
        }
        return 0;
    }

    /** 查询书单中的所有书籍（仅审核通过的书籍对外展示） */
    public List<Book> findBooksByListId(int listId) {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT b.* FROM book b " +
                "INNER JOIN list_book lb ON b.id = lb.book_id " +
                "WHERE lb.list_id = ? AND b.book_status = 'pass' ORDER BY lb.id DESC";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, listId);
            rs = ps.executeQuery();
            while (rs.next()) {
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
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return list;
    }

    /** ResultSet -> BookList 对象（含 nickname / book_count 扩展字段） */
    private BookList buildBookList(ResultSet rs) throws Exception {
        BookList bl = new BookList();
        bl.setId(rs.getInt("id"));
        bl.setUserId(rs.getInt("user_id"));
        bl.setName(rs.getString("name"));
        bl.setIntro(rs.getString("intro"));
        bl.setIsPublic(rs.getInt("is_public"));
        bl.setCreateTime(rs.getTimestamp("create_time"));
        bl.setUpdateTime(rs.getTimestamp("update_time"));
        bl.setNickname(rs.getString("nickname"));
        try {
            bl.setBookCount(rs.getInt("book_count"));
        } catch (Exception ignored) {
            // findById 查询没有 book_count 列，忽略
        }
        return bl;
    }
}
