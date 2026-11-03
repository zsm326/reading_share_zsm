package com.read.entity;

import java.util.Date;

/**
 * 收藏实体类，对应 collect 表
 * targetType：1=书单 2=笔记
 */
public class Collect {
    private Integer id;
    private Integer userId;
    private Integer targetType;
    private Integer targetId;
    private Date createTime;

    // ===== 页面展示扩展字段（不对应数据库列） =====
    private String targetName;   // 书单名称或笔记标题

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getTargetType() { return targetType; }
    public void setTargetType(Integer targetType) { this.targetType = targetType; }

    public Integer getTargetId() { return targetId; }
    public void setTargetId(Integer targetId) { this.targetId = targetId; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }

    public String getTargetName() { return targetName; }
    public void setTargetName(String targetName) { this.targetName = targetName; }
}
