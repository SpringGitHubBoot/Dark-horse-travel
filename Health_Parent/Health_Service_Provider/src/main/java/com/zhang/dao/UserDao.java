package com.zhang.dao;
//@author ZT 2019/7/16-15:37  

import com.github.pagehelper.Page;
import com.zhang.entity.User;

import java.util.HashMap;
import java.util.List;

public interface UserDao {

    User selectUserByUsername(String username);

    String getImg(String username);

    User findUserByUsername(String username);

    Page<User> selectUserPage(String queryString);

    User selectUserById(Integer id);

    List<Integer> selectRolesIdByUserId(Integer id);

    void add(User user);

    void insertTableUserAndRole(HashMap<String, Integer> map);

    void updateUser(User user);

    void deleteFromTableUserAndRoleByUserID(int userId);

    void deleteUserById(Integer userId);
}
