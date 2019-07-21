package com.zhang.entity;

import java.io.Serializable;
import java.util.Date;

public class Order implements Serializable {

    public static final String ORDERTYPE_TELEPHONE = "电话预约";
    public static final String ORDERTYPE_WEIXIN = "微信预约";
    public static final String ORDERSTATUS_YES = "已到诊";
    public static final String ORDERSTATUS_NO = "未到诊";

    private int id;
    private int member_id;
    private Date orderDate;
    private String orderType;
    private String orderStatus;
    private int setmeal_id;

    public Order(int member_id, Date orderDate, int setmeal_id) {
        this.member_id = member_id;
        this.orderDate = orderDate;
        this.setmeal_id = setmeal_id;
    }

    public Order() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setSetmeal_id(int setmeal_id) {
        this.setmeal_id = setmeal_id;
    }

    public int getSetmeal_id() {
        return setmeal_id;
    }
}
