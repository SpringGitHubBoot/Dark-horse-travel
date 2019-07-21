package com.zhang.controller;
//@author ZT 2019/7/12-16:05  

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhang.constance.MessageConstant;
import com.zhang.entity.SetMeal;
import com.zhang.pojo.Result;
import com.zhang.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;

@RestController
@RequestMapping(value = {"/setMeal"})
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;


    @RequestMapping(value = {"/getSetMeal"})
    public Result getSetMeal() {
        try {
            //首次查询redis中没有数据，
            String redisSetMealList = jedisPool.getResource().get("setMealList");
            if (redisSetMealList == null || "".equals(redisSetMealList)) {
                List<SetMeal> setMealList = setMealService.selectAllSetMeal();
                redisSetMealList = JSON.toJSONString(setMealList);
                jedisPool.getResource().set("setMealList", redisSetMealList);
                return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, setMealList);
            }
            //将json字符串转换成list集合
            List<SetMeal> setMeals = JSONObject.parseArray(redisSetMealList, SetMeal.class);
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, setMeals);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    @RequestMapping(value = {"/findById"})
    public Result findById(Integer id) {
        try {
            //首次查询redis中没有数据，
            String idStr = id.toString();
            String redisSetMealDetail = jedisPool.getResource().get(idStr);
            if (redisSetMealDetail == null || "".equals(redisSetMealDetail)) {
                SetMeal setMealDetail = setMealService.findById(id);
                redisSetMealDetail = JSON.toJSONString(setMealDetail);
                jedisPool.getResource().set(idStr, redisSetMealDetail);
                return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, setMealDetail);
            }
            //将json字符串转换成JavaBean对象
            SetMeal setMeal = JSON.parseObject(redisSetMealDetail, SetMeal.class);
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, setMeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
