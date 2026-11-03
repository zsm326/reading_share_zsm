package com.read.action;

import com.opensymphony.xwork2.ActionSupport;
import com.read.dao.BookDao;
import com.read.dao.CollectDao;
import com.read.dao.CommentDao;
import com.read.dao.NoteDao;
import com.read.entity.Book;
import com.read.entity.Comment;
import com.read.entity.Note;
import com.read.entity.User;
import org.apache.struts2.ServletActionContext;

import java.util.List;

/**
 * 读书笔记模块Action：发布、编辑、删除、详情、搜索
 */
public class NoteAction extends ActionSupport {

    private final NoteDao noteDao = new NoteDao();
    private final BookDao bookDao = new BookDao();
    private final CommentDao commentDao = new CommentDao();
    private final CollectDao collectDao = new CollectDao();

    // ===== 表单参数 =====
    private Integer id;
    private Integer bookId;
    private String title;
    private String content;
    private String keyword;

    // ===== 页面数据 =====
    private List<Note> notes;
    private Note note;
    private Book book;
    private List<Comment> comments;
    private boolean collected;
    private int collectCount;

    private User getLoginUser() {
        return (User) ServletActionContext.getRequest().getSession().getAttribute("loginUser");
    }

    /** 笔记列表 / 搜索（所有人可见） */
    public String list() {
        notes = noteDao.search(keyword);
        return SUCCESS;
    }

    /** 我的笔记 */
    public String my() {
        notes = noteDao.findByUserId(getLoginUser().getId());
        return SUCCESS;
    }

    /** 笔记详情（含评论区、收藏状态） */
    public String detail() {
        if (id == null) {
            return NONE;
        }
        note = noteDao.findById(id);
        if (note == null) {
            return NONE;
        }
        comments = commentDao.findByNoteId(id);
        collectCount = collectDao.countByTarget(2, id);
        User loginUser = getLoginUser();
        if (loginUser != null) {
            collected = collectDao.exists(loginUser.getId(), 2, id);
        }
        return SUCCESS;
    }

    /** 发布笔记页面：必须先选择书籍 */
    public String addPage() {
        if (bookId == null) {
            return NONE;
        }
        book = bookDao.findById(bookId);
        if (book == null) {
            return NONE;
        }
        return SUCCESS;
    }

    /** 发布笔记 */
    public String add() {
        if (bookId == null || bookDao.findById(bookId) == null) {
            return NONE;
        }
        if (isBlank(title) || isBlank(content)) {
            addFieldError("note", "笔记标题和内容不能为空");
            return INPUT;
        }
        Note n = new Note();
        n.setUserId(getLoginUser().getId());
        n.setBookId(bookId);
        n.setTitle(title.trim());
        n.setContent(content.trim());
        noteDao.insert(n);
        return SUCCESS;
    }

    /** 编辑页面：仅作者本人 */
    public String editPage() {
        note = noteDao.findById(id);
        if (!isOwner(note)) {
            return NONE;
        }
        return SUCCESS;
    }

    /** 保存修改：仅作者本人 */
    public String update() {
        Note db = noteDao.findById(id);
        if (!isOwner(db)) {
            return NONE;
        }
        if (isBlank(title) || isBlank(content)) {
            addFieldError("note", "笔记标题和内容不能为空");
            return INPUT;
        }
        db.setTitle(title.trim());
        db.setContent(content.trim());
        noteDao.update(db);
        return SUCCESS;
    }

    /** 删除笔记：仅作者本人 */
    public String delete() {
        Note db = noteDao.findById(id);
        if (!isOwner(db)) {
            return NONE;
        }
        noteDao.delete(id);
        return SUCCESS;
    }

    /** 校验当前用户是否为笔记作者 */
    private boolean isOwner(Note n) {
        User loginUser = getLoginUser();
        return n != null && loginUser != null && loginUser.getId().equals(n.getUserId());
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // ===== getter/setter =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public List<Note> getNotes() { return notes; }
    public Note getNote() { return note; }
    public Book getBook() { return book; }
    public List<Comment> getComments() { return comments; }
    public boolean isCollected() { return collected; }
    public int getCollectCount() { return collectCount; }
}
