package com.read.entity;

import java.util.Date;

/**
 * 书单实体类，对应 book_list 表
 */
public class BookList {
    private Integer id;
    private Integer userId;
    private String name;
    private String intro;
    private Integer isPublic;    // 1=公开 0=私有
    private Date createTime;
    private Date updateTime;

    // ===== 以下为页面展示用的扩展字段（不对应数据库列） =====
    private String nickname;     // 创建者昵称
    private Integer bookCount;   // 书单内书籍数量

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIntro() { return intro; }
    public void setIntro(String intro) { this.intro = intro; }

    public Integer getIsPublic() { return isPublic; }
    public void setIsPublic(Integer isPublic) { this.isPublic = isPublic; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public Integer getBookCount() { return bookCount; }
    public void setBookCount(Integer bookCount) { this.bookCount = bookCount; }
}
