package com.zhang.dao;
//@author ZT 2019/7/16-15:37  

import com.zhang.entity.User;

public interface UserDao {

    User selectUserByUsername(String username);

    String getImg(String username);

    User findUserByUsername(String username);
}
