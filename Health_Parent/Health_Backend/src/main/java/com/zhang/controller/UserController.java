package com.zhang.controller;
//@author ZT 2019/7/16-19:04  

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.constance.MessageConstant;
import com.zhang.pojo.Result;
import com.zhang.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(value = {"/user"})
public class UserController {

    @Reference
    private UserService userService;

    @RequestMapping(value = {"/getUsername"})
    public Result getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取框架的用户User对象，然后再获取用户的信息
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        String password = user.getPassword();
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
    }

    @RequestMapping(value = {"/getImg"})
    public Result getImg(String username) {
        try {
            String imgName = userService.getImg(username);
            return new Result(true, MessageConstant.GET_USER_IMG_SUCCESS, imgName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_USER_IMG_FAIL);
        }
    }
}
