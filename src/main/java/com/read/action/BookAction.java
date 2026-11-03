package com.read.action;

import com.opensymphony.xwork2.ActionSupport;
import com.read.dao.BookDao;
import com.read.dao.NoteDao;
import com.read.entity.Book;
import com.read.entity.Note;
import com.read.entity.User;
import com.read.util.CoverUtil;
import com.read.util.UploadUtil;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.util.Date;
import java.util.List;

/**
 * 书籍模块Action：书籍录入（管理员）、搜索、详情
 */
public class BookAction extends ActionSupport {

    private final BookDao bookDao = new BookDao();
    private final NoteDao noteDao = new NoteDao();

    // ===== 表单参数 =====
    private Integer id;
    private String keyword;      // 搜索关键词
    private String name;
    private String author;
    private String isbn;
    private String intro;
    // 封面上传
    private File cover;
    private String coverFileName;

    // ===== 页面数据 =====
    private List<Book> books;
    private Book book;
    private List<Note> notes;    // 该书籍下的笔记

    /** 书籍列表 / 搜索（所有人可访问，未审核封面按规则遮蔽） */
    public String list() {
        books = bookDao.search(keyword);
        User viewer = getLoginUser();
        for (Book b : books) {
            CoverUtil.mask(b, viewer);
        }
        return SUCCESS;
    }

    /** 书籍详情（含该书的所有笔记）；非 pass 书籍仅提交者本人/管理员可见 */
    public String detail() {
        if (id == null) {
            return NONE;
        }
        book = bookDao.findById(id);
        if (book == null) {
            return NONE;
        }
        // 待审核/已驳回的书籍不对外展示：仅提交者本人或管理员可看
        if (!CoverUtil.STATUS_PASS.equals(book.getBookStatus())) {
            User viewer = getLoginUser();
            boolean owner = viewer != null && book.getSubmitUserId() != null
                    && book.getSubmitUserId().equals(viewer.getId());
            boolean admin = viewer != null && viewer.getRole() != null && viewer.getRole() == 1;
            if (!owner && !admin) {
                return NONE;
            }
        }
        // pending 仅上传者本人/管理员可见，reject 所有人不可见
        CoverUtil.mask(book, getLoginUser());
        notes = noteDao.findByBookId(id);
        return SUCCESS;
    }

    /** 分享书籍页面（任意登录用户） */
    public String addPage() {
        return SUCCESS;
    }

    /** 分享书籍（任意登录用户）：普通用户提交后进入待审核，管理员提交直接发布 */
    public String add() {
        User loginUser = (User) ServletActionContext.getRequest()
                .getSession().getAttribute("loginUser");
        if (loginUser == null) {
            return NONE;
        }
        if (isBlank(name) || isBlank(author)) {
            addFieldError("book", "书名和作者不能为空");
            return INPUT;
        }
        boolean admin = loginUser.getRole() != null && loginUser.getRole() == 1;
        Book b = new Book();
        b.setName(name.trim());
        b.setAuthor(author.trim());
        b.setIsbn(isbn == null ? "" : isbn.trim());
        b.setIntro(intro == null ? "" : intro.trim());
        // 提交人：谁分享的就是谁；管理员提交直接发布并成为分享者，普通用户待审核
        b.setSubmitUserId(loginUser.getId());
        b.setBookStatus(admin ? CoverUtil.STATUS_PASS : CoverUtil.STATUS_PENDING);
        b.setUserId(admin ? loginUser.getId() : null);
        // 处理封面上传（可选）：管理员封面直接通过，普通用户封面进入待审核
        if (cover != null) {
            String saveDir = ServletActionContext.getServletContext().getRealPath("/");
            saveDir = saveDir + File.separator + "upload";
            String saved = UploadUtil.saveImage(cover, coverFileName, saveDir);
            if (saved == null) {
                addFieldError("cover", "封面上传失败：仅支持jpg/png/gif且不超过2MB");
                return INPUT;
            }
            b.setCover(saved);
            b.setCoverStatus(admin ? CoverUtil.STATUS_PASS : CoverUtil.STATUS_PENDING);
            b.setCoverUserId(loginUser.getId());
            b.setCoverTime(new Date());
        } else {
            b.setCoverStatus(CoverUtil.STATUS_PENDING);
        }
        bookDao.insert(b);
        return SUCCESS;
    }

    /** 当前登录用户 */
    private User getLoginUser() {
        return (User) ServletActionContext.getRequest()
                .getSession().getAttribute("loginUser");
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // ===== getter/setter =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }

    public File getCover() { return cover; }
    public void setCover(File cover) { this.cover = cover; }

    public String getCoverFileName() { return coverFileName; }
    public void setCoverFileName(String coverFileName) { this.coverFileName = coverFileName; }

    public List<Book> getBooks() { return books; }
    public Book getBook() { return book; }
    public List<Note> getNotes() { return notes; }
}
