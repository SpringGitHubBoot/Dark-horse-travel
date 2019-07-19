package com.zhang.service;
//@author ZT 2019/7/7-16:05  

import com.zhang.entity.CheckGroup;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;

import java.util.List;

public interface CheckGroupService {


    //添加检查组
    void add(CheckGroup checkGroup, Integer[] checkItemIds);

    //分页查询全部检查组数据
    PageResult selectCheckGroupPage(QueryPageBean queryPageBean);

    //根据id查询指定检查组
    CheckGroup selectCheckGroupById(Integer id);

    //根据检查组id查询所有与它关联的检查项的id
    List<Integer> selectCheckItemsByGroupId(Integer id);

    //修改检查组数据
    void update(CheckGroup checkGroup, Integer[] checkItemIds);

    //根据id删除检查组
    void delete(Integer id);

    //查询所有检查组
    List<CheckGroup> selectAllCheckGroup();
}
