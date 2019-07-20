package com.zhang.service;
//@author ZT 2019/7/16-15:33  

import com.zhang.entity.User;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;

import java.util.List;

public interface UserService {

    User findUserByUsername(String username);

    String getImg(String username);

    PageResult selectUserPage(QueryPageBean queryPageBean);

    User selectUserById(Integer id);

    List<Integer> selectRolesIdByUserId(Integer id);

    void add(User user, Integer[] roleIds);

    void update(User user, Integer[] roleIds);

    void delete(Integer id);
}
