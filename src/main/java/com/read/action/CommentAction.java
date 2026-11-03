package com.read.action;

import com.opensymphony.xwork2.ActionSupport;
import com.read.dao.CommentDao;
import com.read.entity.Comment;
import com.read.entity.User;
import org.apache.struts2.ServletActionContext;

/**
 * 评论模块Action：发表评论、删除自己的评论
 */
public class CommentAction extends ActionSupport {

    private final CommentDao commentDao = new CommentDao();

    // ===== 表单参数 =====
    private Integer id;      // 评论id（删除用）
    private Integer noteId;  // 所属笔记id
    private String content;

    private User getLoginUser() {
        return (User) ServletActionContext.getRequest().getSession().getAttribute("loginUser");
    }

    /** 发表评论 */
    public String add() {
        if (noteId == null) {
            return INPUT;
        }
        if (content == null || content.trim().isEmpty()) {
            // 内容为空直接返回详情页（页面也有非空校验）
            return INPUT;
        }
        Comment c = new Comment();
        c.setUserId(getLoginUser().getId());
        c.setNoteId(noteId);
        c.setContent(content.trim());
        commentDao.insert(c);
        return SUCCESS;
    }

    /** 删除评论：仅评论作者本人 */
    public String delete() {
        Comment db = commentDao.findById(id);
        if (db != null) {
            noteId = db.getNoteId();
            User loginUser = getLoginUser();
            // 只有评论作者本人可以删除
            if (loginUser.getId().equals(db.getUserId())) {
                commentDao.delete(id);
            }
        }
        return SUCCESS;
    }

    // ===== getter/setter =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getNoteId() { return noteId; }
    public void setNoteId(Integer noteId) { this.noteId = noteId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
