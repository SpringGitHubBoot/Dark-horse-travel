package com.zhang.service;
//@author ZT 2019/7/13-15:52  

import com.zhang.pojo.Result;

import java.util.Map;

public interface OrderService {

    Result submitOrder(Map map) throws Exception;

    Map findOrderById(Integer id);

    //定时清理预约设置历史数据
    void clearOrderSettingJob(String clearDate);

}
