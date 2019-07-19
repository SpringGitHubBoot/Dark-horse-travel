package com.zhang.service;
//@author ZT 2019/7/17-10:37  

import com.zhang.entity.Menu;

import java.util.List;

public interface MenuService {

    List<Menu> getMenuByUsername(String username);
}
