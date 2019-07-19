package com.zhang.service;
//@author ZT 2019/7/18-15:23  

import com.alibaba.dubbo.config.annotation.Service;
import com.zhang.dao.MemberDao;
import com.zhang.dao.OrderDao;
import com.zhang.dao.OrderSettingDao;
import com.zhang.dao.SetMealDao;
import com.zhang.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private SetMealDao setMealDao;

    @Override
    public Map<String, Object> getBusinessReport() throws Exception{

        //获取当前日期字符串yyyy-MM-dd
        String reportDate = DateUtils.parseDate2String(new Date());

        //获取当天新注册会员的人数
        Integer todayNewMember = memberDao.getTodayNewMember(reportDate);

        //获取总会员人数
        Integer totalMember = memberDao.getTotalMember();

        //获取本周的第一天
        Date firstDayOfWeek = DateUtils.getFirstDayOfWeek(new Date());
        //获取本周的最后一天
        Date lastDayOfWeek = DateUtils.getLastDayOfWeek(new Date());
        //获取本月的第一天
        Date firstDay4ThisMonth = DateUtils.getFirstDay4ThisMonth();
        //获取本月的最后一天
        Date lastDay4ThisMonth = DateUtils.getLastDay4ThisMonth();

        //获取本周新增的会员人数
        Integer thisWeekNewMember = memberDao.getThisWeekNewMember(firstDayOfWeek);

        //获取本月新增的会员人数
        Integer thisMonthNewMember = memberDao.getThisMonthNewMember(firstDay4ThisMonth);

        //获取当天预约的人数
        Integer todayOrderNumber = orderDao.getTodayOrderNumber(reportDate);
        
        //获取当天到诊的人数
        Integer todayVisitsNumber = orderDao.getTodayVisitsNumber(reportDate);

        //获取本周预约的人数
        HashMap<Object, Object> weekMap = new HashMap<>();
        weekMap.put("firstDayOfWeek", firstDayOfWeek);
        weekMap.put("lastDayOfWeek", lastDayOfWeek);
        Integer thisWeekOrderNumber = orderDao.getThisWeekOrderNumber(weekMap);

        //获取本周到诊的人数
        Integer thisWeekVisitsNumber = orderDao.getThisWeekVisitsNumber(weekMap);

        //获取本月预约的人数
        HashMap<Object, Object> monthMap = new HashMap<>();
        monthMap.put("firstDay4ThisMonth", firstDay4ThisMonth);
        monthMap.put("lastDay4ThisMonth", lastDay4ThisMonth);
        Integer thisMonthOrderNumber = orderDao.getThisMonthOrderNumber(monthMap);

        //获取本月到诊的人数
        Integer thisMonthVisitsNumber = orderDao.getThisMonthVisitsNumber(monthMap);

        //获取排名前三的热门套餐
        List<Map> hotSetmeal = setMealDao.getHotSetmeal();

        Map<String, Object> map = new HashMap<>();
        map.put("reportDate", reportDate);
        map.put("todayNewMember", todayNewMember);
        map.put("totalMember", totalMember);
        map.put("thisWeekNewMember", thisWeekNewMember);
        map.put("thisMonthNewMember", thisMonthNewMember);
        map.put("todayOrderNumber", todayOrderNumber);
        map.put("todayVisitsNumber", todayVisitsNumber);
        map.put("thisWeekOrderNumber", thisWeekOrderNumber);
        map.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
        map.put("thisMonthOrderNumber", thisMonthOrderNumber);
        map.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
        map.put("hotSetmeal", hotSetmeal);
        return map;
    }
}
