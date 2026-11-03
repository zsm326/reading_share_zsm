package com.read.action;

import com.opensymphony.xwork2.ActionSupport;
import com.read.dao.CollectDao;
import com.read.entity.Collect;
import com.read.entity.User;
import org.apache.struts2.ServletActionContext;

import java.util.List;

/**
 * 收藏模块Action：收藏/取消收藏书单与笔记，我的收藏
 */
public class CollectAction extends ActionSupport {

    private final CollectDao collectDao = new CollectDao();

    // ===== 表单参数 =====
    private Integer targetType;  // 1书单 2笔记
    private Integer targetId;

    // ===== 页面数据 =====
    private List<Collect> listCollects;  // 收藏的书单
    private List<Collect> noteCollects;  // 收藏的笔记

    private User getLoginUser() {
        return (User) ServletActionContext.getRequest().getSession().getAttribute("loginUser");
    }

    /**
     * 收藏/取消收藏切换：
     * 已收藏则取消，未收藏则添加；返回对应详情页
     */
    public String toggle() {
        User loginUser = getLoginUser();
        if (targetType == null || targetId == null) {
            return "note";
        }
        if (collectDao.exists(loginUser.getId(), targetType, targetId)) {
            collectDao.delete(loginUser.getId(), targetType, targetId);
        } else {
            collectDao.insert(loginUser.getId(), targetType, targetId);
        }
        return targetType == 1 ? "list" : "note";
    }

    /** 我的收藏（分书单和笔记两组展示） */
    public String my() {
        int userId = getLoginUser().getId();
        listCollects = collectDao.findByUser(userId, 1);
        noteCollects = collectDao.findByUser(userId, 2);
        return SUCCESS;
    }

    // ===== getter/setter =====
    public Integer getTargetType() { return targetType; }
    public void setTargetType(Integer targetType) { this.targetType = targetType; }

    public Integer getTargetId() { return targetId; }
    public void setTargetId(Integer targetId) { this.targetId = targetId; }

    public List<Collect> getListCollects() { return listCollects; }
    public List<Collect> getNoteCollects() { return noteCollects; }
}
