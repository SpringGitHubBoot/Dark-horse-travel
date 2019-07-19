package com.zhang.service;
//@author ZT 2019/7/9-17:11  

import com.zhang.entity.SetMeal;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;

import java.util.List;
import java.util.Map;

public interface SetMealService {

    //添加套餐
    void addSetMeal(SetMeal setMeal, Integer[] checkGroupIds);

    //分页查询套餐表
    PageResult selectSetMealPage(QueryPageBean queryPageBean);

    //通过id查询套餐信息
    SetMeal selectSetMealById(Integer id);

    //查询指定id的套餐对应的全部检查组Id
    List<Integer> selectCheckGroupIdBySetMealId(Integer id);

    //编辑套餐信息
    void updateSetMeal(SetMeal setMeal, Integer[] checkgroupIds);

    //删除套餐
    void deleteSetMeal(SetMeal setMeal);

    //查询全部套餐，在移动端显示
    List<SetMeal> selectAllSetMeal();

    //根据id查询套餐信息，包括详细的检查组信息和检查项信息
    SetMeal findById(Integer id);

    //获取套餐预约报表
    Map getSetMealReport();
}
