package com.zhang.service;
//@author ZT 2019/7/16-15:33  

import com.zhang.entity.User;

public interface UserService {

    User findUserByUsername(String username);

    String getImg(String username);
}
