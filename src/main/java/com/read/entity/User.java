package com.read.entity;

import java.util.Date;

/**
 * 用户实体类，对应 user 表
 */
public class User {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatar;
    private Integer role;        // 0=普通用户 1=管理员
    private Date createTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
