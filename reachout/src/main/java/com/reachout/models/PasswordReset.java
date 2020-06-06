package com.reachout.models;

public class PasswordReset {
    private int id;
    private int userId;
    private String code;
    private long createDate;

    public PasswordReset() {

    }

    public PasswordReset(int userId, String code) {
        this.userId = userId;
        this.code = code;
        this.createDate = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }
    
}