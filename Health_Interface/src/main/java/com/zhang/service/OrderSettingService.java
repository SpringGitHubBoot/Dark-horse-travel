package com.zhang.service;
//@author ZT 2019/7/10-18:36  

import com.zhang.entity.OrderSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    //批量导入预约设置信息
    void addOrderSetting(ArrayList<OrderSetting> orderSettingList);

    //获取当前月的预约数据
    List<Map<String, Integer>> getOrderSettingByMonth(String currentMonth) throws Exception;

    //修改某天可预约人数，正常写法
    void setOrderPeopleNumber(OrderSetting orderSetting) throws Exception;

    /*//修改某天可预约人数，尝试写法
    void setOrderPeopleNumber(String orderSetting);*/
}
