package com.zhang.service;
//@author ZT 2019/7/6-16:02  

import com.zhang.entity.CheckItem;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;

import java.util.List;

public interface CheckItemService {

    //添加检查项的方法
    void addCheckItem(CheckItem checkItem);

    //分页查询检查项的方法
    PageResult selectPageData(QueryPageBean queryPageBean);

    //根据id删除检查项
    void deleteById(Integer id);

    //根据id查询一条检查项并返回
    CheckItem selectCheckItemById(Integer id);

    //编辑检查项
    void updateCheckItem(CheckItem checkItem);

    //查询所有的检查项
    List<CheckItem> selectAllCheckItem();
}
