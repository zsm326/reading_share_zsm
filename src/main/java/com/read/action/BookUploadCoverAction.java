package com.read.action;

import com.opensymphony.xwork2.ActionSupport;
import com.read.dao.BookDao;
import com.read.entity.Book;
import com.read.entity.User;
import com.read.util.CoverUtil;
import com.read.util.UploadUtil;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 书籍封面Action：
 * 1. page()   打开「上传封面」页面（任意登录用户）
 * 2. upload() 上传封面 → 校验类型/大小 → UUID重命名保存到 webapp/upload
 *
 * 审核规则：
 * - 普通用户上传：cover_status = pending（待管理员审核）
 * - 管理员上传：cover_status = pass（直接通过，沿用原有管理员逻辑）
 */
public class BookUploadCoverAction extends ActionSupport {

    /** 允许的封面后缀（仅 jpg、png） */
    private static final List<String> ALLOW_EXT = Arrays.asList("jpg", "jpeg", "png");
    /** 最大 2MB */
    private static final long MAX_SIZE = 2 * 1024 * 1024;

    private final BookDao bookDao = new BookDao();

    // ===== 表单参数 =====
    private Integer id;              // 书籍id
    private File cover;              // Struts2 注入的上传文件
    private String coverFileName;    // 原始文件名

    // ===== 页面数据 =====
    private Book book;               // 当前书籍（用于回显旧封面）

    /** 打开上传封面页面（仅该书分享者/管理员） */
    public String page() {
        User loginUser = getLoginUser();
        if (loginUser == null || id == null) {
            return NONE;
        }
        book = bookDao.findById(id);
        if (book == null) {
            return NONE;
        }
        // 归属校验：只有该书分享者（审核通过后的提交人）或管理员才能进入上传页
        if (!canUpload(book, loginUser)) {
            return deny();
        }
        // 无权查看的封面不展示（pending 仅上传者本人/管理员可见）
        CoverUtil.mask(book, loginUser);
        return SUCCESS;
    }

    /** 上传并更新封面（仅该书分享者/管理员） */
    public String upload() {
        User loginUser = getLoginUser();
        if (loginUser == null || id == null) {
            return NONE;
        }
        // 先查出书籍，页面回显和存在性校验都用它
        book = bookDao.findById(id);
        if (book == null) {
            return NONE;
        }
        // 归属校验：防止绕过前端直接 POST 上传接口
        if (!canUpload(book, loginUser)) {
            return deny();
        }
        // 1. 非空校验
        if (cover == null || coverFileName == null || coverFileName.trim().isEmpty()) {
            addFieldError("cover", "请选择要上传的封面图片");
            return INPUT;
        }
        // 2. 后缀校验：只允许 jpg / jpeg / png
        String ext = coverFileName.substring(coverFileName.lastIndexOf('.') + 1).toLowerCase();
        if (!ALLOW_EXT.contains(ext)) {
            addFieldError("cover", "封面只能是 jpg 或 png 格式");
            return INPUT;
        }
        // 3. 大小校验：不超过 2MB
        if (cover.length() > MAX_SIZE) {
            addFieldError("cover", "封面图片不能超过 2MB");
            return INPUT;
        }
        // 4. 保存到 webapp/upload（用站点根目录拼接，目录不存在时 UploadUtil 会自动创建）
        String saveDir = ServletActionContext.getServletContext().getRealPath("/")
                + File.separator + "upload";
        String savedName = UploadUtil.saveImage(cover, coverFileName, saveDir);
        if (savedName == null || savedName.isEmpty()) {
            addFieldError("cover", "封面上传失败，请重试");
            return INPUT;
        }
        // 5. 管理员直接通过，普通用户进入待审核
        boolean admin = loginUser.getRole() != null && loginUser.getRole() == 1;
        String status = admin ? CoverUtil.STATUS_PASS : CoverUtil.STATUS_PENDING;
        if (bookDao.updateCover(id, savedName, status, loginUser.getId()) == 0) {
            addFieldError("cover", "封面保存失败，请重试");
            return INPUT;
        }
        // 6. redirect 后仍要提示，信息放 session（详情页读取后清除）
        ServletActionContext.getRequest().getSession()
                .setAttribute("message", admin ? "封面已更新" : "封面提交成功，等待管理员审核");
        return SUCCESS;
    }

    /** 当前登录用户 */
    private User getLoginUser() {
        return (User) ServletActionContext.getRequest()
                .getSession().getAttribute("loginUser");
    }

    /** 归属校验：管理员 或 该书分享者（user_id，审核通过后才赋值）才能上传封面 */
    private boolean canUpload(Book b, User u) {
        if (u.getRole() != null && u.getRole() == 1) {
            return true;    // 管理员
        }
        return b.getUserId() != null && b.getUserId().equals(u.getId());  // 分享者本人
    }

    /** 无权操作：写提示到 session（redirect 后详情页读取并清除），返回 NONE 跳回详情页 */
    private String deny() {
        ServletActionContext.getRequest().getSession()
                .setAttribute("message", "无权操作：只有该书的分享者或管理员才能上传封面");
        return NONE;
    }

    // ===== getter/setter =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public File getCover() { return cover; }
    public void setCover(File cover) { this.cover = cover; }

    public String getCoverFileName() { return coverFileName; }
    public void setCoverFileName(String coverFileName) { this.coverFileName = coverFileName; }

    public Book getBook() { return book; }
}
