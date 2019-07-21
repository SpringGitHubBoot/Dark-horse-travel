package com.zhang.dao;
//@author ZT 2019/7/6-16:13  

import com.github.pagehelper.Page;
import com.zhang.entity.CheckItem;

import java.util.List;

public interface CheckItemDao {

    void addCheckItem(CheckItem checkItem);

    Page<CheckItem> selectPageData(String keyword);

    int selectCheckItemIDInCC(Integer id);

    void deleteCheckItemById(Integer id);

    CheckItem selectCheckItemById(Integer id);

    void updateCheckItem(CheckItem checkItem);

    List<CheckItem> selectAllCheckItem();

    List<Integer> findCheckGroupIds(Integer checkItemId);
}
