package com.zhang.entity;

import java.io.Serializable;
import java.util.List;

public class CheckGroup implements Serializable{
    private int id;
    private String code;
    private String name;
    private String helpCode;
    private String sex;
    private String remark;
    private String attention;
    private List<CheckItem> checkItemList;

    public List<CheckItem> getCheckItemList() {
        return checkItemList;
    }

    public void setCheckItemList(List<CheckItem> checkItemList) {
        this.checkItemList = checkItemList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setHelpCode(String helpCode) {
        this.helpCode = helpCode;
    }

    public String getHelpCode() {
        return helpCode;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSex() {
        return sex;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemark() {
        return remark;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getAttention() {
        return attention;
    }
}
