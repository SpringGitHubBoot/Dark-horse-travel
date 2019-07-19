package com.zhang.dao;
//@author ZT 2019/7/16-15:52  

import com.zhang.entity.Permission;

import java.util.Set;

public interface PermissionDao {
    Set<Permission> findPermissionsByRoleId(int roleId);
}
