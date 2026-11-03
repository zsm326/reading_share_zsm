package com.read.entity;

import java.util.Date;

/**
 * 读书笔记实体类，对应 note 表
 */
public class Note {
    private Integer id;
    private Integer userId;
    private Integer bookId;
    private String title;
    private String content;
    private Date createTime;
    private Date updateTime;

    // ===== 页面展示扩展字段（不对应数据库列） =====
    private String nickname;     // 作者昵称
    private String bookName;     // 书籍名称

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }
}
