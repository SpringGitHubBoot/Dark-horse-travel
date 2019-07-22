package com.zhang.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhang.dao.RoleDao;
import com.zhang.entity.Role;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> selectAllRole() {
        return roleDao.selectAllRole();
    }

    @Override
    public PageResult selectPageData(QueryPageBean queryPageBean) {
        String queryString = queryPageBean.getQueryString();
        Integer pageSize = queryPageBean.getPageSize();
        Integer currentPage = queryPageBean.getCurrentPage();
        if (queryString == null) {
            queryString = "";
        }
        PageHelper.startPage(currentPage, pageSize);
        Page<Role> page = roleDao.selectPageData("%" + queryString + "%");
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(Map map) {
        List<Integer> menuIdList = (List<Integer>) map.get("menuIds");
        List<Integer> permissionIdList = (List<Integer>) map.get("permissionIds");
        String string = map.get("formDate").toString();
        Role role = JSON.parseObject(string, Role.class);
        roleDao.addRole(role);
        int roleId = role.getId();
        this.InsertDataToRoleAndMenuAndPermission(roleId, menuIdList, permissionIdList);
    }

    @Override
    public Role queryRoleById(Integer id) {
        return roleDao.selectRoleById(id);
    }

    @Override
    public void update(Map map) {
        List<Integer> menuIdList = (List<Integer>) map.get("menuIds");
        List<Integer> permissionIdList = (List<Integer>) map.get("permissionIds");
        String string = map.get("formDate").toString();
        Role role = JSON.parseObject(string, Role.class);
        int roleId = role.getId();
        roleDao.deleteRoleIdAndMenuIdByRoleId(roleId);
        roleDao.deleteRoleIdAndPermissionIdByRoleId(roleId);
        roleDao.updateRole(role);
        this.InsertDataToRoleAndMenuAndPermission(roleId, menuIdList, permissionIdList);
    }

    @Override
    public void delete(Integer id) {
        roleDao.deleteRoleIdAndMenuIdByRoleId(id);
        roleDao.deleteRoleIdAndPermissionIdByRoleId(id);
        roleDao.deleteRoleIdAndUserIdByRoleId(id);
        roleDao.deleteRoleById(id);
    }

    public void InsertDataToRoleAndMenuAndPermission(Integer roleId, List<Integer> menuIdList, List<Integer> permissionIdList) {
        Map menuAndRoleMap = new HashMap<>();
        Map permissionAndRoleMap = new HashMap<>();
        menuAndRoleMap.put("roleId", roleId);
        permissionAndRoleMap.put("roleId", roleId);

        for (Integer menuId : menuIdList) {
            menuAndRoleMap.put("menuId", menuId);
            roleDao.insertIntoRoleAndMenu(menuAndRoleMap);
        }

        for (Integer permissionId : permissionIdList) {
            permissionAndRoleMap.put("permissionId", permissionId);
            roleDao.insertIntoRoleAndPermission(permissionAndRoleMap);
        }
    }
}
