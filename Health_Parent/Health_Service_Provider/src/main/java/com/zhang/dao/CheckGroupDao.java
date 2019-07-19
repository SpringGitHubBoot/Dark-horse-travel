package com.zhang.dao;
//@author ZT 2019/7/7-16:13  

import com.github.pagehelper.Page;
import com.zhang.entity.CheckGroup;

import java.util.HashMap;
import java.util.List;

public interface CheckGroupDao {

    //添加检查组的dao接口方法
    void add(CheckGroup checkGroup);

    //在中间关系表中添加数据的方法
    void insertTableCheckGroupAndCheckItem(HashMap<String, Integer> map);

    //分页查询检查组数据
    Page<CheckGroup> selectCheckGroupPage(String queryString);

    //根据检查组的id查询一条检查组并且返回
    CheckGroup selectCheckGroupById(Integer id);

    //根据checkGroupId字段查询关系表中的checkItemId字段并将其全部返回
    List<Integer> selectCheckItemsByGroupId(Integer id);

    //编辑检查组的内容
    void updateCheckGourp(CheckGroup checkGroup);

    //删除该检查组对应的原检查项id
    void deleteFromTableGroupAndItemByCheckGroupID(Integer checkGroupId);

    void deleteCheckGroupIdInAssociationTable(Integer id);

    void deleteCheckGroupById(Integer id);

    List<CheckGroup> selectAllCheckGroup();
}
