package com.zhang.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.constance.MessageConstant;
import com.zhang.entity.Role;
import com.zhang.pojo.Result;
import com.zhang.service.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = {"/role"})
public class RoleController {

    @Reference
    private RoleService roleService;

    @RequestMapping(value = {"/selectAllRole"})
    public Result selectAllRole() {
        try {
            //注意dubbo传递实体类数据则必须要序列化
            List<Role> roleList = roleService.selectAllRole();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, roleList);
        } catch (Exception e) {
            //通过是否发生异常来得到返回给前端的数据，数据用Result来封装
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }





}
