package com.zhang.dao;
//@author ZT 2019/7/16-15:52  

import com.github.pagehelper.Page;
import com.zhang.entity.CheckItem;
import com.zhang.entity.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionDao {
    Set<Permission> findPermissionsByRoleId(int roleId);

    Page<Permission> selectPageData(String queryString);

    void addPermission(Permission permission);

    Permission selectPermissionById(Integer id);

    void deleteById(Integer id);

    void updatePermission(Permission permission);

    List<Permission> selectAllPermission();
}
