package com.read.entity;

import java.util.Date;

/**
 * 评论实体类，对应 comment 表
 */
public class Comment {
    private Integer id;
    private Integer userId;
    private Integer noteId;
    private String content;
    private Date createTime;

    // ===== 页面展示扩展字段（不对应数据库列） =====
    private String nickname;     // 评论者昵称
    private String noteTitle;    // 所属笔记标题（管理后台用）

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getNoteId() { return noteId; }
    public void setNoteId(Integer noteId) { this.noteId = noteId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getNoteTitle() { return noteTitle; }
    public void setNoteTitle(String noteTitle) { this.noteTitle = noteTitle; }
}
