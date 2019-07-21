package com.zhang.security;
//@author ZT 2019/7/16-15:11  

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.entity.Permission;
import com.zhang.entity.Role;
import com.zhang.entity.User;
import com.zhang.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Set;

@Component(value = "securityService")
public class UserSecurityService implements UserDetailsService {

    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //查询用户表看看用户输入的用户名是否存在
        User userInDb = userService.findUserByUsername(username);
        //用户名错误，直接返回null，用户无法登陆
        if (userInDb == null) {
            return null;
        }

        //用户名正确，此时需要拿到用户对应的所有角色和权限
        ArrayList<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roleSet = userInDb.getRoleSet();
        //判断用户对应的角色列表是否为null
        if (roleSet != null && roleSet.size() > 0) {
            //不为空，遍历用户对应的角色列表
            for (Role role : roleSet) {
                String roleKeyword = role.getKeyword();
                //将用户对应的角色关键字添加到角色权限集合list
                list.add(new SimpleGrantedAuthority(roleKeyword));
                //得到用户角色对应的权限了列表
                Set<Permission> permissionSet = role.getPermissionSet();
                //判断该权限列表是否为null
                if (permissionSet != null && permissionSet.size() > 0) {
                    //不为空，则遍历角色对应的权限
                    for (Permission permission : permissionSet) {
                        String keyword = permission.getKeyword();
                        //将权限关键字添加到角色权限集合中
                        list.add(new SimpleGrantedAuthority(keyword));
                    }
                }
            }
        }
        //最后，给框架的user对象赋值，并将它返回，用以和用户输入的密码匹配
        return new org.springframework.security.core.userdetails.User(
                username, userInDb.getPassword(), list
        );
    }
}
