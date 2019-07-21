package com.zhang.dao;
//@author ZT 2019/7/13-16:26  

import com.zhang.entity.Order;

import java.util.Date;
import java.util.Map;

public interface OrderDao {

    Order selectOrderByThree(Order order);

    void add(Order order);

    Map findOrderById(Integer id);

    Integer getTodayOrderNumber(String reportDate);

    Integer getTodayVisitsNumber(String reportDate);

    Integer getThisWeekOrderNumber(Map map);

    Integer getThisWeekVisitsNumber(Map map);

    Integer getThisMonthOrderNumber(Map map);

    Integer getThisMonthVisitsNumber(Map map);

    //定时删除order表数据
    void clearOrderSettingJob(String date);
}
