package com.zhang.service;
//@author ZT 2019/7/13-16:04  

import com.alibaba.dubbo.config.annotation.Service;
import com.zhang.constance.MessageConstant;
import com.zhang.dao.MemberDao;
import com.zhang.dao.OrderDao;
import com.zhang.dao.OrderSettingDao;
import com.zhang.dao.SetMealDao;
import com.zhang.entity.Member;
import com.zhang.entity.Order;
import com.zhang.entity.OrderSetting;
import com.zhang.pojo.Result;
import com.zhang.util.DateUtils;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;


    @Override
    public Result submitOrder(Map map) throws Exception {
        /*
         * 业务逻辑：
         *   1.判断用户是否想要在没有提供预约的日期进行预约
         *   2.判断用户预约的日期，可预约人数是否已满
         *   3.判断用户是否进行了重复预约，同一用户在同一天预约同一个套餐
         *   4.如果当前用户不是会员，使其自动注册成为会员
         *   5.在预约设置表中，将可预约人数+1
         *   6.将预约信息插入到预约表中，并将前端需要的数据返回
         */

        //判断用户是否想要在没有提供预约的日期进行预约
        String orderDate = (String) map.get("orderDate");
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.selectOrderSettingByDate(date);
        if (orderSetting == null) {
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //判断用户预约的日期，可预约人数是否已满
        if (orderSetting.getReservations() >= orderSetting.getNumber()) {
            return new Result(false, MessageConstant.ORDER_FULL);
        }

        //判断用户是否进行了重复预约，同一用户在同一天预约同一个套餐
        String telephone = (String) map.get("telephone");
        //用用户手机号来获取该用户信息数据
        Member member = memberDao.selectMemberByPhoneNumber(telephone);
        Integer setmealId = Integer.parseInt((String) map.get("setmealId"));
        System.out.println(setmealId);
        if (member != null) {
            int memberId = member.getId();

            Order order = new Order(memberId, date, setmealId);
            order = orderDao.selectOrderByThree(order);
            if (order != null) {
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        }

        //如果当前用户不是会员，使其自动注册成为会员
        if (member == null) {
            String idCard = (String) map.get("idCard");
            String name = (String) map.get("name");
            String sex = (String) map.get("sex");
            Date registerDate = new Date();

            member = new Member();
            member.setIdCard(idCard);
            member.setRegTime(registerDate);
            member.setName(name);
            member.setSex(sex);
            member.setPhoneNumber(telephone);

            memberDao.add(member);
        }

        //将预约设置表中的可预约人数修改
        orderSettingDao.updateOrderSettingReservations(date);

        //将预约信息插入预约表中
        String orderType = (String) map.get("orderType");
        String orderStatus = (String) map.get("orderStatus");
        Order order = new Order();
        order.setMember_id(member.getId());
        order.setOrderDate(date);
        order.setOrderType(orderType);
        order.setOrderStatus(orderStatus);
        order.setSetmeal_id(setmealId);
        orderDao.add(order);

        //将插入的数据查询出来响应给前端
        Integer id = order.getId();
        return new Result(true, MessageConstant.ORDER_SUCCESS, id);
    }

    @Override
    public Map findOrderById(Integer id) {
        return orderDao.findOrderById(id);
    }


    //定时清理预约设置历史数据
    public void clearOrderSettingJob(String date) {
        date += "-31";

        orderDao.clearOrderSettingJob(date);//2019-07-31
    }
}
