package com.zhang.controller;
//@author ZT 2019/7/17-10:35  

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.constance.MessageConstant;
import com.zhang.entity.Menu;
import com.zhang.pojo.Result;
import com.zhang.service.MenuService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = {"/menu"})
public class MenuController {
    @Reference
    private MenuService menuService;

    @RequestMapping(value = {"/getMenuByUsername"})
    public Result getMenuByUsername(String username) {
        try {
            List<Menu> menus = menuService.getMenuByUsername(username);
            return new Result(true, MessageConstant.GET_MENU_SUCCESS, menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }
    }

    

}
