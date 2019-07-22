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

@RestController
@RequestMapping(value = {"/role"})
public class RoleController {
    @Reference
    private RoleService roleService;
    //查询所有角色
    @RequestMapping(value = {"/selectAllRole"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_QUERY')")
    public Result selectAllRole(){
        try {
            List<Role> roleList = roleService.selectAllRole();
            return new Result(true, MessageConstant.QUERY_SETMEAL_ROLE_SUCCESS,roleList);
        } catch (Exception e) {
            return new Result(false, MessageConstant.QUERY_SETMEAL_ROLE_FAIL);
        }
    }
    //分页查询
    @RequestMapping(value = {"/selectPageData"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_QUERY')")
    public PageResult selectRolePageData(@RequestBody QueryPageBean queryPageBean){
        return roleService.selectPageData(queryPageBean);
    }
    //添加角色
    @RequestMapping(value = {"/add"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_ADD')")
    public Result addRole(@RequestBody Role role){
        try {
            roleService.addRole(role);
        } catch (Exception e) {
            return new Result(false, MessageConstant.ADD_ROLE_FAIL);
        }
        return new Result(true, MessageConstant.ADD_ROLE_SUCCESS);
    }
    //编辑角色
    @RequestMapping(value = {"/update"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_EDIT')")
    public Result updateRole(@RequestBody Role role){
        try {
            roleService.updateRole(role);
        } catch (Exception e) {
            return new Result(false, MessageConstant.EDIT_ROLE_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_ROLE_SUCCESS);
    }
    //回显
    @RequestMapping(value = {"/queryRole"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_QUERY')")
    public Result queryRoleById(Integer id){
        Role role = roleService.selectRoleById(id);
            return new Result(role);
    }
    //删除角色
    @RequestMapping(value = {"/delete"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_DELETE')")
    public Result deleteRole(Integer id){
        try {
            roleService.deleteById(id);
        } catch (Exception e) {
            return new Result(false, MessageConstant.DELETE_ROLE_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_ROLE_SUCCESS);
    }
}
