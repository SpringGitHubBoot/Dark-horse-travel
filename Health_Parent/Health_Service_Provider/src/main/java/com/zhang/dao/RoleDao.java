package com.zhang.dao;
//@author ZT 2019/7/16-15:51  

import com.zhang.entity.Role;

import java.util.Set;

public interface RoleDao {

    Set<Role> selectRoleListByUserId(int id);
}
