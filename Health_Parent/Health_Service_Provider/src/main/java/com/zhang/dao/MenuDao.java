package com.zhang.dao;
//@author ZT 2019/7/17-10:44  

import com.github.pagehelper.Page;
import com.zhang.entity.Menu;

import java.util.List;

public interface MenuDao {

    List<Menu> getMenusByUsername(String username);

    Page<Menu> selectMenuPageData(String keyWord);

    Menu findMenuByPath(String path);

    int getMaxTurnNumber();

    void addMenu(Menu menu);

    void updateMenuTurnsAfterInsert(int turn);

    Menu queryMenuById(Integer id);

    void updateMenu(Menu menu);

    void updateMenuTurnsAfterDelete(int turn);

    void deleteMenuById(Integer id);

    List<Menu> findMenuListByPartPath(String childPath);
}
