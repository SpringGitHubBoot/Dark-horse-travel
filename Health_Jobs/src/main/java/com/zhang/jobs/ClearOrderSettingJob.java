package com.zhang.jobs;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.service.OrderService;
import com.zhang.util.DateUtils;

/**
 * @author Kaki Nakajima
 * @ProjectGroup longGroup
 * @描述 定时清理预约设置历史数据
 * @需求 预约设置（OrderSetting）数据是用来设置未来每天的可预约人数，
 *      随着时间的推移预约设置表的数据会越来越多，而对于已经过去的历史数据可以定时来进行清理，
 *      例如每月最后一天凌晨2点执行一次清理任务
 */

public class ClearOrderSettingJob {

    //调用数据库查询预约表
    @Reference
    private OrderService orderService;

    //定时清理预约设置历史数据
    public void clearOrderSettingJob() throws Exception {
        //获取当前月份
        String date = DateUtils.parseDate2String(DateUtils.getToday(), "yyyy-MM");
        //按照当前月份执行删除当前月份数据
        orderService.clearOrderSettingJob(date);
    }

}
