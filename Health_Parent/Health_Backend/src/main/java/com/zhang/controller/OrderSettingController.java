package com.zhang.controller;
//@author ZT 2019/7/10-17:07  

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.constance.MessageConstant;
import com.zhang.entity.OrderSetting;
import com.zhang.pojo.Result;
import com.zhang.service.OrderSettingService;
import com.zhang.util.POIUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/orderSetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping(value = "/upload")
    @PreAuthorize(value = "hasAnyAuthority('ORDERSETTING')")
    public Result addOrderSetting(@RequestParam(value = "excelFile") MultipartFile excelFile) {

        try {

            List<String[]> stringList = POIUtils.readExcel(excelFile);
            ArrayList<OrderSetting> orderSettingList = new ArrayList<>();
            for (String[] strings : stringList) {
                OrderSetting orderSetting = new OrderSetting();
                Date date = new SimpleDateFormat("yyyy/MM/dd").parse(strings[0]);
                orderSetting.setOrderDate(date);
                orderSetting.setNumber(Integer.parseInt(strings[1]));

                orderSettingList.add(orderSetting);
            }

            orderSettingService.addOrderSetting(orderSettingList);

            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);

        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);

        }
    }

    @RequestMapping(value = {"/getOrderSettingByMonth"})
    @PreAuthorize(value = "hasAnyAuthority('ORDERSETTING')")
    public Result getOrderSettingByMonth(String currentMonth) {

        try {
            List<Map<String, Integer>> list = orderSettingService.getOrderSettingByMonth(currentMonth);
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping(value = {"/setOrderPeopleNumber"})
    @PreAuthorize(value = "hasAnyAuthority('ORDERSETTING')")
    public Result setOrderPeopleNumber(@RequestBody OrderSetting orderSetting) {

        try {

            orderSettingService.setOrderPeopleNumber(orderSetting);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
    }

    /*@RequestMapping(value = {"/setOrderPeopleNumber"})
    public Result setOrderPeopleNumber(@RequestBody String today) {

        try {
//          得到的数据是：  {"today":"2019-07-13T00:00:00.000Z"}，不方便使用
            orderSettingService.setOrderPeopleNumber(today);
            return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.ORDERSETTING_FAIL);
        }
    }*/
}
