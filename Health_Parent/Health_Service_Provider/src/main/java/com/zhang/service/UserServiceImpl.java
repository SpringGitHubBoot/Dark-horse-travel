package com.zhang.service;
//@author ZT 2019/7/16-15:35  

import com.alibaba.dubbo.config.annotation.Service;
import com.zhang.dao.PermissionDao;
import com.zhang.dao.RoleDao;
import com.zhang.dao.UserDao;
import com.zhang.entity.Permission;
import com.zhang.entity.Role;
import com.zhang.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

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
}
