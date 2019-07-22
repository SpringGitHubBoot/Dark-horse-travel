package com.zhang.controller;
//@author ZT 2019/7/17-10:35  

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.constance.MessageConstant;
import com.zhang.entity.Menu;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.pojo.Result;
import com.zhang.service.MenuService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = {"/menu"})
public class MenuController {

    @Reference
    private MenuService menuService;

    @RequestMapping(value = {"/getMenuByUsername"})
    @PreAuthorize(value = "hasAnyAuthority('MENU_QUERY')")
    public Result getMenuByUsername(String username) {
        try {
            List<Menu> menus = menuService.getMenuByUsername(username);
            return new Result(true, MessageConstant.GET_MENU_SUCCESS, menus);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_MENU_FAIL);
        }
    }

    @RequestMapping(value = {"/selectMenuPageData"})
    @PreAuthorize(value = "hasAnyAuthority('MENU_QUERY')")
    public PageResult selectMenuPageData(@RequestBody QueryPageBean pageRequestData) {
        return menuService.selectMenuPageData(pageRequestData);
    }

    @RequestMapping(value = {"/addMenu"})
    @PreAuthorize(value = "hasAnyAuthority('MENU_ADD')")
    public Result addMenu(@RequestBody Menu menu) {
        try {
            menuService.addMenu(menu);
            return new Result(true, MessageConstant.ADD_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_MENU_FAIL);
        }
    }

    @RequestMapping(value = {"/queryMenuById"})
    @PreAuthorize(value = "hasAnyAuthority('MENU_QUERY')")
    public Result queryMenuById(Integer id) {
        try {
            Menu menu = menuService.queryMenuById(id);
            return new Result(true, MessageConstant.QUERY_MENU_SUCCESS, menu);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_MENU_FAIL);
        }
    }

    @RequestMapping(value = {"/updateMenu"})
    @PreAuthorize(value = "hasAnyAuthority('MENU_EDIT')")
    public Result updateMenu(@RequestBody Menu menu) {
        try {
            menuService.updateMenu(menu);
            return new Result(true, MessageConstant.EDIT_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_MENU_FAIL);
        }
    }

    @RequestMapping(value = {"/deleteMenu"})
    @PreAuthorize(value = "hasAnyAuthority('MENU_DELETE')")
    public Result deleteMenu(Integer id) {
        try {
            menuService.deleteMenu(id);
            return new Result(true, MessageConstant.DELETE_MENU_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_MENU_FAIL);
        }
    }

    @RequestMapping(value = {"/selectAllMenu"})
    @PreAuthorize(value = "hasAnyAuthority('MENU_QUERY')")
    public Result selectAllMenu() {
        try {
            List<Menu> menuList = menuService.selectAllMenu();
            return new Result(true, MessageConstant.QUERY_MENU_SUCCESS, menuList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_MENU_FAIL);
        }
    }

    @RequestMapping(value = {"/selectMenuIdsByRoleId"})
    @PreAuthorize(value = "hasAnyAuthority('MENU_QUERY')")
    public Result selectMenuIdsByRoleId(Integer id) {
        try {
            List<Integer> menuIds = menuService.selectMenuIdsByRoleId(id);
            return new Result(true, MessageConstant.QUERY_MENU_SUCCESS, menuIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_MENU_FAIL);
        }
    }
}
