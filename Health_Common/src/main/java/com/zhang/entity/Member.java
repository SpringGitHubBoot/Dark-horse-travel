package com.zhang.entity;

import java.io.Serializable;
import java.util.Date;

public class Member implements Serializable {
    private int id;
    private String fileNumber;
    private String name;
    private String sex;
    private String idCard;
    private String phoneNumber;
    private Date regTime;
    private String password;
    private String email;
    private Date birthday;
    private String remark;



    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setRegTime(Date regTime) {
        this.regTime = regTime;
    }

    public Date getRegTime() {
        return regTime;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }
}
