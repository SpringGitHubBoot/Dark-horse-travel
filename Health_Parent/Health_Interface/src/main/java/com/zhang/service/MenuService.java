package com.zhang.service;
//@author ZT 2019/7/17-10:37  

import com.zhang.entity.Menu;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;

import java.util.List;

public interface MenuService {

    List<Menu> getMenuByUsername(String username);

    PageResult selectMenuPageData(QueryPageBean pageRequestData);

    void addMenu(Menu menu) throws Exception;

    Menu queryMenuById(Integer id);

    void updateMenu(Menu menu);

    void deleteMenu(Integer id) throws Exception;
}
