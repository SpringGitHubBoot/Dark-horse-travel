package com.zhang.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhang.dao.PermissionDao;
import com.zhang.dao.RoleDao;
import com.zhang.entity.CheckItem;
import com.zhang.entity.Permission;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private RoleDao roleDao;

    @Override
    public PageResult selectPageData(QueryPageBean queryPageBean) {

        //使用分页插件，传入两个参数当前页和每页记录条数
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //调用持久层方法，注意用Page来接收
        String keyWord = queryPageBean.getQueryString();
        if (keyWord == null) {
            keyWord = "";
        }
        Page<Permission> page = permissionDao.selectPageData("%" + keyWord + "%");

        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void addPermission(Permission permission) {
        permissionDao.addPermission(permission);
    }

    @Override
    public Permission selectPermissionById(Integer id) {

        return permissionDao.selectPermissionById(id);
    }

    @Override
    public void deleteById(Integer id) {
        permissionDao.deleteById(id);
    }

    @Override
    public void updatePermission(Permission permission) {
        permissionDao.updatePermission(permission);
    }

    @Override
    public List<Permission> selectAllPermission() {
        return permissionDao.selectAllPermission();
    }

    @Override
    public List<Integer> selectPermissionIdsByRoleId(Integer id) {
        return roleDao.getPermissionIds(id);
    }
}
