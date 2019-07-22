package com.zhang.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhang.dao.RoleDao;
import com.zhang.entity.Role;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleDao roleDao;
    //添加角色
    public void addRole(Role role) {
        roleDao.addRole(role);
    }

    //查询所有角色
    public List<Role> selectAllRole() {
        return roleDao.selectAllRole();
    }
    //分页查询
    public PageResult selectPageData(QueryPageBean queryPageBean) {
        //使用分页插件，传入两个参数当前页和每页记录条数
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //调用持久层方法，注意用Page来接收
        Page<Role> page = roleDao.selectPageData(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(), page.getResult());
    }
    //编辑角色
    public void updateRole(Role role) {
        roleDao.updateRole(role);
    }
    //回显角色
    public Role selectRoleById(Integer id) {
        return roleDao.selectRoleById(id);
    }

    //删除角色
    public void deleteById(Integer id) {
        int line = roleDao.selectRoleIDInCC(id);
        if (line>0){
            throw new RuntimeException();
        }else {
            roleDao.deleteRoleById(id);
        }
    }
}
