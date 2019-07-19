package com.zhang.entity;

import java.io.Serializable;
import java.util.Date;

public class OrderSetting implements Serializable {
    private int id;
    private Date orderDate;
    private int number;
    private int reservations;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setReservations(int reservations) {
        this.reservations = reservations;
    }

    public int getReservations() {
        return reservations;
    }
}
