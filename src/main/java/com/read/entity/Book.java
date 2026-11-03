package com.read.entity;

import java.util.Date;

/**
 * 书籍实体类，对应 book 表
 */
public class Book {
    private Integer id;
    private String name;
    private String author;
    private String isbn;
    private String cover;
    private String intro;
    /** 封面审核状态：pending 待审核 / pass 通过 / reject 驳回 */
    private String coverStatus;
    /** 封面最后上传人id */
    private Integer coverUserId;
    /** 封面上传时间 */
    private Date coverTime;
    /** 非表字段：审核列表里展示的上传人昵称（联表查询填充） */
    private String coverUserName;
    /** 分享者id（审核通过时 = 提交人id），封面上传权限以此判断 */
    private Integer userId;
    /** 提交人id（谁分享的这本书，提交时即写入） */
    private Integer submitUserId;
    /** 非表字段：书籍审核列表里展示的提交人昵称（联表查询填充） */
    private String submitUserName;
    /** 书籍审核状态：pending 待审核 / pass 通过（公开展示） / reject 驳回 */
    private String bookStatus;
    private Date createTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getCover() { return cover; }
    public void setCover(String cover) { this.cover = cover; }

    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }

    public String getCoverStatus() { return coverStatus; }
    public void setCoverStatus(String coverStatus) { this.coverStatus = coverStatus; }

    public Integer getCoverUserId() { return coverUserId; }
    public void setCoverUserId(Integer coverUserId) { this.coverUserId = coverUserId; }

    public Date getCoverTime() { return coverTime; }
    public void setCoverTime(Date coverTime) { this.coverTime = coverTime; }

    public String getCoverUserName() { return coverUserName; }
    public void setCoverUserName(String coverUserName) { this.coverUserName = coverUserName; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getSubmitUserId() { return submitUserId; }
    public void setSubmitUserId(Integer submitUserId) { this.submitUserId = submitUserId; }

    public String getSubmitUserName() { return submitUserName; }
    public void setSubmitUserName(String submitUserName) { this.submitUserName = submitUserName; }

    public String getBookStatus() { return bookStatus; }
    public void setBookStatus(String bookStatus) { this.bookStatus = bookStatus; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
