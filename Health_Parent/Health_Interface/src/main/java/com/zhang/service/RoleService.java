package com.zhang.service;

import com.zhang.entity.Role;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;

import java.util.List;
import java.util.Map;

public interface RoleService {

    List<Role> selectAllRole();

    PageResult selectPageData(QueryPageBean queryPageBean);

    void add(Map map);

    Role queryRoleById(Integer id);

    void update(Map map);

    void delete(Integer id);
}
