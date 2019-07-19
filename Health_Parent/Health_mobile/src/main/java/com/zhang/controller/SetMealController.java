package com.zhang.controller;
//@author ZT 2019/7/12-16:05  

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.constance.MessageConstant;
import com.zhang.entity.SetMeal;
import com.zhang.pojo.Result;
import com.zhang.service.SetMealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = {"/setMeal"})
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @RequestMapping(value = {"/getSetMeal"})
    public Result getSetMeal() {
        try {
            List<SetMeal> setMealList = setMealService.selectAllSetMeal();
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, setMealList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    @RequestMapping(value = {"/findById"})
    public Result findById(Integer id) {
        try {
            SetMeal setMeal = setMealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setMeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
