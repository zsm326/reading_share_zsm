package com.read.entity;

import java.util.Date;

/**
 * 书单-书籍关联实体类，对应 list_book 表
 */
public class ListBook {
    private Integer id;
    private Integer listId;
    private Integer bookId;
    private Date createTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getListId() { return listId; }
    public void setListId(Integer listId) { this.listId = listId; }

    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }

    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
