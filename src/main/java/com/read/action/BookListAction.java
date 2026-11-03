package com.read.action;

import com.opensymphony.xwork2.ActionSupport;
import com.read.dao.BookDao;
import com.read.dao.BookListDao;
import com.read.dao.CollectDao;
import com.read.entity.Book;
import com.read.entity.BookList;
import com.read.entity.User;
import com.read.util.CoverUtil;
import org.apache.struts2.ServletActionContext;

import java.util.List;

/**
 * 书单模块Action：创建/编辑/删除书单，书单书籍管理，公开书单浏览
 */
public class BookListAction extends ActionSupport {

    private final BookListDao bookListDao = new BookListDao();
    private final BookDao bookDao = new BookDao();
    private final CollectDao collectDao = new CollectDao();

    // ===== 表单参数 =====
    private Integer id;          // 书单id
    private Integer bookId;      // 添加/移除的书籍id
    private String name;
    private String intro;
    private Integer isPublic;    // 1公开 0私有
    private String keyword;      // 书单详情页搜索可添加书籍

    // ===== 页面数据 =====
    private List<BookList> bookLists;
    private BookList bookList;
    private List<Book> books;        // 书单内书籍
    private List<Book> searchBooks;  // 可添加的搜索结果
    private boolean collected;       // 当前用户是否已收藏
    private int collectCount;        // 被收藏次数

    private User getLoginUser() {
        return (User) ServletActionContext.getRequest().getSession().getAttribute("loginUser");
    }

    /** 公开书单广场（所有人可见） */
    public String publicList() {
        bookLists = bookListDao.findPublic();
        return SUCCESS;
    }

    /** 我的书单 */
    public String my() {
        bookLists = bookListDao.findByUserId(getLoginUser().getId());
        return SUCCESS;
    }

    /** 书单详情：私有书单仅创建者可见 */
    public String detail() {
        if (id == null) {
            return NONE;
        }
        bookList = bookListDao.findById(id);
        if (bookList == null) {
            return NONE;
        }
        User loginUser = getLoginUser();
        // 私有书单且不是创建者 -> 无权查看
        if (bookList.getIsPublic() == 0
                && (loginUser == null || !loginUser.getId().equals(bookList.getUserId()))) {
            return NONE;
        }
        books = bookListDao.findBooksByListId(id);
        // 未通过审核的封面按规则遮蔽（pending 仅本人/管理员，reject 所有人）
        for (Book book : books) {
            CoverUtil.mask(book, loginUser);
        }
        collectCount = collectDao.countByTarget(1, id);
        if (loginUser != null) {
            collected = collectDao.exists(loginUser.getId(), 1, id);
        }
        // 创建者可在详情页搜索书籍加入书单
        if (loginUser != null && loginUser.getId().equals(bookList.getUserId()) && keyword != null) {
            searchBooks = bookDao.search(keyword);
            for (Book book : searchBooks) {
                CoverUtil.mask(book, loginUser);
            }
        }
        return SUCCESS;
    }

    /** 创建书单 */
    public String add() {
        if (isBlank(name)) {
            addFieldError("name", "书单名称不能为空");
            return INPUT;
        }
        BookList bl = new BookList();
        bl.setUserId(getLoginUser().getId());
        bl.setName(name.trim());
        bl.setIntro(intro == null ? "" : intro.trim());
        bl.setIsPublic(isPublic != null && isPublic == 0 ? 0 : 1);
        bookListDao.insert(bl);
        return SUCCESS;
    }

    /** 编辑页面：仅创建者 */
    public String editPage() {
        bookList = bookListDao.findById(id);
        if (!isOwner(bookList)) {
            return NONE;
        }
        return SUCCESS;
    }

    /** 保存修改：仅创建者 */
    public String update() {
        BookList db = bookListDao.findById(id);
        if (!isOwner(db)) {
            return NONE;
        }
        if (isBlank(name)) {
            addFieldError("name", "书单名称不能为空");
            bookList = db;
            return INPUT;
        }
        db.setName(name.trim());
        db.setIntro(intro == null ? "" : intro.trim());
        db.setIsPublic(isPublic != null && isPublic == 0 ? 0 : 1);
        bookListDao.update(db);
        return SUCCESS;
    }

    /** 删除书单：仅创建者 */
    public String delete() {
        BookList db = bookListDao.findById(id);
        if (!isOwner(db)) {
            return NONE;
        }
        bookListDao.delete(id);
        return SUCCESS;
    }

    /** 向书单添加书籍：仅创建者 */
    public String addBook() {
        BookList db = bookListDao.findById(id);
        if (!isOwner(db) || bookId == null) {
            return NONE;
        }
        bookListDao.addBook(id, bookId);
        return SUCCESS;
    }

    /** 从书单移除书籍：仅创建者 */
    public String removeBook() {
        BookList db = bookListDao.findById(id);
        if (!isOwner(db) || bookId == null) {
            return NONE;
        }
        bookListDao.removeBook(id, bookId);
        return SUCCESS;
    }

    /** 校验当前用户是否为书单创建者 */
    private boolean isOwner(BookList bl) {
        User loginUser = getLoginUser();
        return bl != null && loginUser != null && loginUser.getId().equals(bl.getUserId());
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // ===== getter/setter =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }

    public Integer getIsPublic() { return isPublic; }
    public void setIsPublic(Integer isPublic) { this.isPublic = isPublic; }

    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }

    public List<BookList> getBookLists() { return bookLists; }
    public BookList getBookList() { return bookList; }
    public List<Book> getBooks() { return books; }
    public List<Book> getSearchBooks() { return searchBooks; }
    public boolean isCollected() { return collected; }
    public int getCollectCount() { return collectCount; }
}
