package com.zhang.dao;
//@author ZT 2019/7/16-15:51  

import com.github.pagehelper.Page;
import com.zhang.entity.Role;

import java.util.List;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RoleDao {
    //编辑角色
    void updateRole(Role role);
    //  添加角色
    void addRole(Role role);
    Set<Role> selectRoleListByUserId(int id);
//  查询所有角色
    List<Role> selectAllRole();
//  分页查询
    Page<Role> selectPageData(String queryString);
//  回显角色
    Role selectRoleById(Integer id);
//删除角色，先查关联后删
    int selectRoleIDInCC(Integer id);
    void deleteRoleById(Integer id);

    void insertIntoRoleAndMenu(Map map);

    List<Integer> getRoleIdsAboutMenuId(Integer id);

    void deleteFromRoleAndMenuByIds(Map map);

}
