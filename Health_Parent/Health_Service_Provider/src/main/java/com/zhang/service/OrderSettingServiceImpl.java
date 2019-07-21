package com.zhang.service;
//@author ZT 2019/7/10-18:37  

import com.alibaba.dubbo.config.annotation.Service;
import com.zhang.dao.OrderSettingDao;
import com.zhang.entity.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    public void addOrderSetting(ArrayList<OrderSetting> orderSettingList) {

        if (orderSettingList != null && orderSettingList.size() > 0) {
            for (OrderSetting orderSetting : orderSettingList) {

                Date orderDate = orderSetting.getOrderDate();
                //实现批量插入功能，不能插入重复的数据，首先判断要插入表的记录是否已存在
                int line = orderSettingDao.selectOrderSettingByOrderDate(orderDate);
                //如果返回数值大于0，说明已经存在，直接更新数据即可，
                //否则表示当天无预约设置，需要插入当天的数据
                if (line > 0) {
                    orderSettingDao.updateOrderSetting(orderSetting);
                } else {
                    orderSettingDao.addOrderSetting(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String currentMonth) throws Exception {

        String begin = currentMonth + "-1";
        String end = currentMonth + "-31";
        Date beginDate = new SimpleDateFormat("yyyy-MM-dd").parse(begin);
        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(end);
        HashMap<String, Date> map = new HashMap<>();
        map.put("beginDate", beginDate);
        map.put("endDate", endDate);
        List<OrderSetting> orderSettingList = orderSettingDao.getOrderSettingByMonth(map);

        ArrayList<Map<String, Integer>> list = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (OrderSetting orderSetting : orderSettingList) {

            Map<String, Integer> hashMap = new HashMap<>();
            calendar.setTime(orderSetting.getOrderDate());
            hashMap.put("date", calendar.get(Calendar.DATE));
            hashMap.put("number", orderSetting.getNumber());
            hashMap.put("reservations", orderSetting.getReservations());

            list.add(hashMap);
        }
        return list;
    }


    @Override
    public void setOrderPeopleNumber(OrderSetting orderSetting) throws Exception{
        if (orderSetting.getNumber() == 0) {
            throw new Exception();
        }
        orderSettingDao.setOrderPeopleNumber(orderSetting);
    }

    /*@Override
    public void setOrderPeopleNumber(String orderSetting) {

    }*/
}
