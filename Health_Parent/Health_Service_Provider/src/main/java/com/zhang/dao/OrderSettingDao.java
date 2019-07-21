package com.zhang.dao;
//@author ZT 2019/7/10-18:39  

import com.zhang.entity.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    //通过预约的日期查询当天是否有预约设置数据
    Integer selectOrderSettingByOrderDate(Date orderDate);

    //这天的预约设置数据已经存在，在原数据基础上实现更新
    void updateOrderSetting(OrderSetting orderSetting);

    //这天的预约设置数据不存在，添加新的数据
    void addOrderSetting(OrderSetting orderSetting);

    //查询当前月的预约数据
    List<OrderSetting> getOrderSettingByMonth(Map<String, Date> map);

    void setOrderPeopleNumber(OrderSetting orderSetting);

    OrderSetting selectOrderSettingByDate(Date date);

    void updateOrderSettingReservations(Date date);
}
