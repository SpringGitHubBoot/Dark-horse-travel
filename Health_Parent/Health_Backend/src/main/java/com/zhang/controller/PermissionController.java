package com.zhang.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.constance.MessageConstant;
import com.zhang.entity.Permission;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.pojo.Result;
import com.zhang.service.PermissionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    PermissionService permissionService;

    @RequestMapping(value = {"/add"})
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public Result addCheckItem(@RequestBody Permission permission) {
        try {
            //注意dubbo传递实体类数据则必须要序列化
            permissionService.addPermission(permission);
        } catch (Exception e) {
            //通过是否发生异常来得到返回给前端的数据，数据用Result来封装
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL);
        }
        return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
    }


    //获取当前页的权限数据
    @RequestMapping("/selectPageData")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public PageResult selectPageData(@RequestBody QueryPageBean queryPageBean) {
        return permissionService.selectPageData(queryPageBean);
    }

    @RequestMapping(value = {"/queryPermission"})
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public Result queryPermissionById(Integer id) {
        Permission permission = permissionService.selectPermissionById(id);
        return new Result(permission);
    }


    @RequestMapping(value = {"/delete"})
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public Result deletePermission(Integer id) {

        try {
            //注意dubbo传递实体类数据则必须要序列化
            permissionService.deleteById(id);
        } catch (Exception e) {
            //通过是否发生异常来得到返回给前端的数据，数据用Result来封装
            return new Result(false, MessageConstant.DELETE_PERMISSION_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
    }


    @RequestMapping(value = {"/update"})
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public Result updatePermission(@RequestBody Permission permission) {

        try {
            //注意dubbo传递实体类数据则必须要序列化
            permissionService.updatePermission(permission);
        } catch (Exception e) {
            //通过是否发生异常来得到返回给前端的数据，数据用Result来封装
            return new Result(false, MessageConstant.EDIT_PERMISSION_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
    }

    @RequestMapping(value = {"/selectAllPermission"})
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public Result selectAllPermission() {

        try {
            List<Permission> permissionList = permissionService.selectAllPermission();
            return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, permissionList);
        } catch (Exception e) {
            //通过是否发生异常来得到返回给前端的数据，数据用Result来封装
            return new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }

    @RequestMapping(value = {"/selectPermissionIdsByRoleId"})
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public Result selectPermissionIdsByRoleId(Integer id) {

        try {
            List<Integer> permissionIds = permissionService.selectPermissionIdsByRoleId(id);
            return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, permissionIds);
        } catch (Exception e) {
            //通过是否发生异常来得到返回给前端的数据，数据用Result来封装
            return new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
        }
    }
}
