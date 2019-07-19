package com.zhang.dao;
//@author ZT 2019/7/17-10:44  

import com.zhang.entity.Menu;

import java.util.List;

public interface MenuDao {
    List<Menu> getMenusByUsername(String username);
}
