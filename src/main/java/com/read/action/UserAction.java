package com.read.action;

import com.opensymphony.xwork2.ActionSupport;
import com.read.entity.User;
import com.read.dao.UserDao;
import com.read.util.MD5Util;
import com.read.util.UploadUtil;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpSession;
import java.io.File;

/**
 * 用户模块Action：注册、登录、退出、修改昵称/密码/头像
 */
public class UserAction extends ActionSupport {

    private final UserDao userDao = new UserDao();

    // ===== 表单参数 =====
    private String username;
    private String password;
    private String confirmPassword;
    private String nickname;
    private String oldPassword;
    // 头像上传（Struts2 文件上传三件套）
    private File avatar;
    private String avatarFileName;

    // ===== 页面回显数据 =====
    private User user;

    /** 获取当前登录用户 */
    private User getLoginUser() {
        HttpSession session = ServletActionContext.getRequest().getSession();
        return (User) session.getAttribute("loginUser");
    }

    /** 注册 */
    public String register() {
        // 非空校验
        if (isBlank(username) || isBlank(password) || isBlank(nickname)) {
            addFieldError("register", "用户名、密码、昵称均不能为空");
            return INPUT;
        }
        if (!password.equals(confirmPassword)) {
            addFieldError("register", "两次输入的密码不一致");
            return INPUT;
        }
        if (userDao.findByUsername(username.trim()) != null) {
            addFieldError("register", "用户名已被注册");
            return INPUT;
        }
        User u = new User();
        u.setUsername(username.trim());
        u.setPassword(MD5Util.encrypt(password));
        u.setNickname(nickname.trim());
        userDao.insert(u);
        // 注册成功，写一条 session 级提示，redirect 到登录页后可以读到
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.setAttribute("message", "注册成功，请使用新账号登录");
        return SUCCESS;
    }

    /** 登录 */
    public String login() {
        if (isBlank(username) || isBlank(password)) {
            // GET方式打开登录页或表单为空
            if (isBlank(username) && isBlank(password)) {
                return NONE;    // 直接显示登录页
            }
            addFieldError("login", "用户名和密码不能为空");
            return INPUT;
        }
        User u = userDao.findByUsername(username.trim());
        if (u == null || !u.getPassword().equals(MD5Util.encrypt(password))) {
            addFieldError("login", "用户名或密码错误");
            return INPUT;
        }
        // 登录成功，用户信息放入session
        HttpSession session = ServletActionContext.getRequest().getSession();
        session.setAttribute("loginUser", u);
        return SUCCESS;
    }

    /** 退出登录 */
    public String logout() {
        ServletActionContext.getRequest().getSession().invalidate();
        return SUCCESS;
    }

    /** 个人中心页面 */
    public String profile() {
        // 重新从数据库读取，保证昵称/头像为最新
        user = userDao.findById(getLoginUser().getId());
        return SUCCESS;
    }

    /** 修改昵称 */
    public String updateNickname() {
        User loginUser = getLoginUser();
        if (isBlank(nickname)) {
            addFieldError("nickname", "昵称不能为空");
            user = userDao.findById(loginUser.getId());
            return INPUT;
        }
        userDao.updateNickname(loginUser.getId(), nickname.trim());
        loginUser.setNickname(nickname.trim());   // 同步session
        return SUCCESS;
    }

    /** 修改密码 */
    public String updatePassword() {
        User loginUser = getLoginUser();
        if (isBlank(oldPassword) || isBlank(password) || isBlank(confirmPassword)) {
            addFieldError("password", "原密码、新密码均不能为空");
            user = userDao.findById(loginUser.getId());
            return INPUT;
        }
        if (!password.equals(confirmPassword)) {
            addFieldError("password", "两次输入的新密码不一致");
            user = userDao.findById(loginUser.getId());
            return INPUT;
        }
        User dbUser = userDao.findById(loginUser.getId());
        if (!dbUser.getPassword().equals(MD5Util.encrypt(oldPassword))) {
            addFieldError("password", "原密码错误");
            user = dbUser;
            return INPUT;
        }
        userDao.updatePassword(loginUser.getId(), MD5Util.encrypt(password));
        return SUCCESS;
    }

    /** 上传/更新头像 */
    public String updateAvatar() {
        User loginUser = getLoginUser();
        String saveDir = ServletActionContext.getServletContext().getRealPath("/upload");
        String saved = UploadUtil.saveImage(avatar, avatarFileName, saveDir);
        if (saved == null) {
            addFieldError("avatar", "头像上传失败：仅支持jpg/png/gif且不超过2MB");
            user = userDao.findById(loginUser.getId());
            return INPUT;
        }
        if (!saved.isEmpty()) {
            userDao.updateAvatar(loginUser.getId(), saved);
            loginUser.setAvatar(saved);
        }
        return SUCCESS;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // ===== getter/setter（Struts2 注入与页面取值用） =====
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public File getAvatar() { return avatar; }
    public void setAvatar(File avatar) { this.avatar = avatar; }

    public String getAvatarFileName() { return avatarFileName; }
    public void setAvatarFileName(String avatarFileName) { this.avatarFileName = avatarFileName; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
