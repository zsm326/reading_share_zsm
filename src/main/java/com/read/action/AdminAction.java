package com.read.action;

import com.opensymphony.xwork2.ActionSupport;
import com.read.dao.BookDao;
import com.read.dao.CommentDao;
import com.read.dao.NoteDao;
import com.read.dao.UserDao;
import com.read.entity.Book;
import com.read.entity.Comment;
import com.read.entity.Note;
import com.read.entity.User;
import com.read.util.CoverUtil;
import org.apache.struts2.ServletActionContext;

import java.util.List;

/**
 * 管理员后台Action：用户管理、违规笔记/评论删除、书籍管理、封面审核
 * （过滤器已拦截非管理员访问，此处做二次校验）
 */
public class AdminAction extends ActionSupport {

    private final UserDao userDao = new UserDao();
    private final NoteDao noteDao = new NoteDao();
    private final CommentDao commentDao = new CommentDao();
    private final BookDao bookDao = new BookDao();

    // ===== 参数 =====
    private Integer id;

    // ===== 页面数据 =====
    private List<User> users;
    private List<Note> notes;
    private List<Comment> comments;
    private List<Book> books;
    private List<Book> pendingCovers;   // 待审核封面列表
    private List<Book> pendingBooks;    // 待审核书籍列表

    /** 校验当前用户是否为管理员 */
    private boolean isAdmin() {
        User loginUser = (User) ServletActionContext.getRequest()
                .getSession().getAttribute("loginUser");
        return loginUser != null && loginUser.getRole() == 1;
    }

    /** 用户列表 */
    public String users() {
        if (!isAdmin()) {
            return NONE;
        }
        users = userDao.findAll();
        return SUCCESS;
    }

    /** 全部笔记 */
    public String notes() {
        if (!isAdmin()) {
            return NONE;
        }
        notes = noteDao.search(null);
        return SUCCESS;
    }

    /** 删除违规笔记 */
    public String deleteNote() {
        if (!isAdmin() || id == null) {
            return NONE;
        }
        noteDao.delete(id);
        return SUCCESS;
    }

    /** 全部评论 */
    public String comments() {
        if (!isAdmin()) {
            return NONE;
        }
        comments = commentDao.findAll();
        return SUCCESS;
    }

    /** 删除违规评论 */
    public String deleteComment() {
        if (!isAdmin() || id == null) {
            return NONE;
        }
        commentDao.delete(id);
        return SUCCESS;
    }

    /** 书籍列表（全部状态，含待审核/已驳回） */
    public String books() {
        if (!isAdmin()) {
            return NONE;
        }
        books = bookDao.searchAll(null);
        return SUCCESS;
    }

    /** 录入书籍页面 */
    public String bookAddPage() {
        if (!isAdmin()) {
            return NONE;
        }
        return SUCCESS;
    }

    /** 删除书籍 */
    public String deleteBook() {
        if (!isAdmin() || id == null) {
            return NONE;
        }
        bookDao.delete(id);
        return SUCCESS;
    }

    /** 书籍审核列表：所有待审核（pending）的书籍 */
    public String bookAudits() {
        if (!isAdmin()) {
            return NONE;
        }
        pendingBooks = bookDao.findPendingBooks();
        return SUCCESS;
    }

    /** 通过发布：书籍公开展示，提交人成为该书分享者 */
    public String approveBook() {
        if (!isAdmin() || id == null) {
            return NONE;
        }
        bookDao.approveBook(id);
        return SUCCESS;
    }

    /** 驳回：书籍不对外展示 */
    public String rejectBook() {
        if (!isAdmin() || id == null) {
            return NONE;
        }
        bookDao.rejectBook(id);
        return SUCCESS;
    }

    /** 封面审核列表：所有待审核（pending）且已上传图片的书籍 */
    public String coverAudits() {
        if (!isAdmin()) {
            return NONE;
        }
        pendingCovers = bookDao.findPendingCovers();
        return SUCCESS;
    }

    /** 通过审核：封面变 pass，所有人可见 */
    public String approveCover() {
        if (!isAdmin() || id == null) {
            return NONE;
        }
        bookDao.updateCoverStatus(id, CoverUtil.STATUS_PASS);
        return SUCCESS;
    }

    /** 驳回：封面变 reject，所有人不可见，记录保留 */
    public String rejectCover() {
        if (!isAdmin() || id == null) {
            return NONE;
        }
        bookDao.updateCoverStatus(id, CoverUtil.STATUS_REJECT);
        return SUCCESS;
    }

    // ===== getter/setter =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public List<User> getUsers() { return users; }
    public List<Note> getNotes() { return notes; }
    public List<Comment> getComments() { return comments; }
    public List<Book> getBooks() { return books; }
    public List<Book> getPendingCovers() { return pendingCovers; }
    public List<Book> getPendingBooks() { return pendingBooks; }
}
