package com.zhang.dao;
//@author ZT 2019/7/9-17:17  

import com.github.pagehelper.Page;
import com.zhang.entity.SetMeal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SetMealDao {

    //添加套餐
    void addSetMeal(SetMeal setMeal);

    //添加套餐和检查组的关联关系
    void addRelationOfSetMealAndCheckGroup(HashMap<String, Integer> map);

    //分页查询套餐表
    Page<SetMeal> selectSetMealPage(String queryString);

    //通过id值查询套餐信息
    SetMeal selectSetMealById(Integer id);

    //通过套餐id查询其指定的检查组id列表
    List<Integer> selectCheckGroupIdBySetMealId(Integer id);

    //编辑套餐信息
    void updateSetMeal(SetMeal setMeal);

    //在中间表中删除套餐id
    void deleteSetMealIdFromMid(int setMealId);

    //删除套餐和检查组中间表中套餐id是指定值的所有记录
    void deleteRelationOfSetMealAndCheckGroup(int setMealId);

    //中间表关联关系删除后，根据id值删除套餐
    void deleteSetMealById(int setMealId);

    //查询全部套餐，并将信息返回给移动端
    List<SetMeal> selectAllSetMeal();

    //根据套餐id查询套餐详细信息，包括检查组合检查项
    SetMeal findById(Integer id);

    List<Map> selectSetMealReport();

    List<Map> getHotSetmeal();

    //当前页数据列表
    Page<Map> getSetMeaList(String queryString) throws Exception;

}
