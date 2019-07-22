package com.zhang.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.constance.MessageConstant;
import com.zhang.entity.Role;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.pojo.Result;
import com.zhang.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = {"/role"})
public class RoleController {

    @Reference
    private RoleService roleService;

    //查询所有角色
    @RequestMapping(value = {"/selectAllRole"})
    @PreAuthorize(value = "hasAnyAuthority('ROLE_QUERY')")
    public Result selectAllRole() {
        try {
            List<Role> roleList = roleService.selectAllRole();
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, roleList);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
    }

    //分页查询
    @RequestMapping(value = {"/selectPageData"})
    @PreAuthorize(value = "hasAnyAuthority('ROLE_QUERY')")
    public PageResult selectRolePageData(@RequestBody QueryPageBean queryPageBean) {
        return roleService.selectPageData(queryPageBean);
    }

    //添加角色
    @RequestMapping(value = {"/add"})
    @PreAuthorize(value = "hasAnyAuthority('ROLE_ADD')")
    public Result add(@RequestBody Map map) {
        try {
            roleService.add(map);
            return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_ROLE_FAIL);
        }
    }

    //根据角色Id查询角色
    @RequestMapping(value = {"/queryRoleById"})
    @PreAuthorize(value = "hasAnyAuthority('ROLE_QUERY')")
    public Result queryRoleById(Integer id) {
        try {
            Role role = roleService.queryRoleById(id);
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, role);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
    }


    //编辑角色信息
    @RequestMapping(value = {"/update"})
    @PreAuthorize(value = "hasAnyAuthority('ROLE_EDIT')")
    public Result update(@RequestBody Map map) {
        try {
            roleService.update(map);
            return new Result(true, MessageConstant.EDIT_ROLE_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_ROLE_FAIL);
        }
    }

    //删除角色信息
    @RequestMapping(value = {"/delete"})
    @PreAuthorize(value = "hasAnyAuthority('ROLE_DELETE')")
    public Result delete(Integer id) {
        try {
            roleService.delete(id);
            return new Result(true, MessageConstant.DELETE_ROLE_SUCCESS);
        } catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_ROLE_FAIL);
        }
    }

}
