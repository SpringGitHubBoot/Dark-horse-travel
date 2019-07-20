package com.zhang.controller;
//@author ZT 2019/7/13-15:42  

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.constance.MessageConstant;
import com.zhang.constance.RedisMessageConstant;
import com.zhang.entity.Order;
import com.zhang.pojo.Result;
import com.zhang.service.OrderService;
import com.zhang.util.DateUtils;
import com.zhang.util.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(value = {"/order"})
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @RequestMapping(value = {"/submitOrder"})
    public Result submitOrder(@RequestBody Map map) throws Exception {

        //判断验证码是否正确
        String validateCode = (String) map.get("validateCode");
        String telephone = (String) map.get("telephone");
        String redisValidateCode = jedisPool.getResource().
                get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        if (redisValidateCode == null || !redisValidateCode.equals(validateCode)) {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

        //将预约类型和是否到诊传入集合中
        map.put("orderType", Order.ORDERTYPE_WEIXIN);
        map.put("orderStatus", Order.ORDERSTATUS_NO);

        try {
             Result result = orderService.submitOrder(map);

            //如果最终返回的flag为true
            if (result.isFlag()) {
                //给用户发送体检预约成功的通知短息
                SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, telephone);
            }

             return result;
        }catch (Exception e){
            e.printStackTrace();
            return new Result(true, MessageConstant.QUERY_ORDER_FAIL);
        }

    }

    @RequestMapping(value = {"/findById"})
    public Result findOrderById(Integer id) {
        try {
            //优化日期的格式，使其不带时分秒
            Map map = orderService.findOrderById(id);
            Date orderDate = (Date) map.get("orderDate");
            String string = DateUtils.parseDate2String(orderDate);
            map.put("orderDate", string);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
