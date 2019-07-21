package com.zhang.service;

import com.zhang.entity.Role;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;

import java.util.List;

public interface RoleService {
    void addRole(Role role);
    //查询所有角色
    List<Role> selectAllRole();
    //分页查询
    PageResult selectPageData(QueryPageBean queryPageBean);
    //编辑角色
    void updateRole(Role role);
    //回显
    Role selectRoleById(Integer id);
    //删除角色
    void deleteById(Integer id);
}
