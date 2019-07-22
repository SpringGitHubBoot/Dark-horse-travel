package com.zhang.service;



import com.zhang.entity.Permission;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;

import java.util.List;

public interface PermissionService {
    PageResult selectPageData(QueryPageBean queryPageBean);

    void addPermission(Permission permission);

    Permission selectPermissionById(Integer id);

    void deleteById(Integer id);

    void updatePermission(Permission permission);

    List<Permission> selectAllPermission();

    List<Integer> selectPermissionIdsByRoleId(Integer id);
}
