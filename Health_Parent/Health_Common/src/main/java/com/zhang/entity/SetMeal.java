package com.zhang.entity;

import java.io.Serializable;
import java.util.List;

public class SetMeal implements Serializable {
    private int id;
    private String name;
    private String code;
    private String helpCode;
    private String sex;
    private String age;
    private float price;
    private String remark;
    private String attention;
    private String img;
    private List<CheckGroup> checkGroupList;

    public List<CheckGroup> getCheckGroupList() {
        return checkGroupList;
    }

    public void setCheckGroupList(List<CheckGroup> checkGroupList) {
        this.checkGroupList = checkGroupList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
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

    public void setAge(String age) {
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
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

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }
}
