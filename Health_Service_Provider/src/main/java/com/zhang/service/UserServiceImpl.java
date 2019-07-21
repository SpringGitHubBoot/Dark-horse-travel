package com.zhang.service;
//@author ZT 2019/7/16-15:35  

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhang.dao.PermissionDao;
import com.zhang.dao.RoleDao;
import com.zhang.dao.UserDao;
import com.zhang.entity.CheckGroup;
import com.zhang.entity.Permission;
import com.zhang.entity.Role;
import com.zhang.entity.User;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public User findUserByUsername(String username) {
        //方式一：调用一次持久层返回完整的user对象，持久层代码比较复杂
        /*return userDao.selectUserByUsername(username);*/
        //方式二：多次调用持久层，业务逻辑更加清晰
        //先查询用户
        User user = userDao.findUserByUsername(username);
        //获取用户id,再查询用户角色中间表和角色表
        int id = user.getId();
        //获得用户对应的角色列表
        Set<Role> roleList = roleDao.selectRoleListByUserId(id);
        //遍历角色列表查询每个角色对应的权限列表
        for (Role role : roleList) {
            int roleId = role.getId();
            Set<Permission> permissionList =
                    permissionDao.findPermissionsByRoleId(roleId);
            role.setPermissionSet(permissionList);
        }
        user.setRoleSet(roleList);
        return user;
    }

    @Override
    public String getImg(String username) {
        return userDao.getImg(username);
    }


    /*使用第三方工具查找用户的信息*/
    @Override
    public PageResult selectUserPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        String queryString = queryPageBean.getQueryString();
        Integer pageSize = queryPageBean.getPageSize();
        PageHelper.startPage(currentPage, pageSize);

        Page<User> page = userDao.selectUserPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /*通过id查找用户*/
    @Override
    public User selectUserById(Integer id) {
        return userDao.selectUserById(id);
    }

    /*根据用户的id查找对应的角色的id列表*/
    @Override
    public List<Integer> selectRolesIdByUserId(Integer id) {
        return userDao.selectRolesIdByUserId(id);
    }

    //添加用户的同时，需要在关联表中建立用户和角色的关系
    //也就是在中间表中添加多个联合主键
    @Override
    public void add(User user, Integer[] roleIds) {
        userDao.add(user);
        int userId = user.getId();
        this.insertTableUserAndRole(userId, roleIds);
    }

    @Override
    public void update(User user, Integer[] roleIds) {
        //更新用户表中的数据
        userDao.updateUser(user);
        //获取用户的id
        int userId = user.getId();
        //通过用户的id删除所有与角色项的关联数据
        userDao.deleteFromTableUserAndRoleByUserID(userId);
        //将用户对应新的角色插入到关系表中
        this.insertTableUserAndRole(userId, roleIds);
    }

    /*根据用户id删除用户*/
    @Override
    public void delete(Integer userId) {
        //通过用户的id删除所有与角色项的关联数据
        userDao.deleteFromTableUserAndRoleByUserID(userId);
        userDao.deleteUserById(userId);
    }

    //抽离的方法，用来在t_user_role表中插入值
    public void insertTableUserAndRole(Integer userId, Integer[] roleIds) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("userId", userId);
        for (Integer roleId : roleIds) {
            map.put("roleId", roleId);
            userDao.insertTableUserAndRole(map);
        }
    }
}
