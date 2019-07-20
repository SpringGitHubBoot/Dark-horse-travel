package com.zhang.service;


import com.alibaba.dubbo.config.annotation.Service;
import com.zhang.dao.RoleDao;
import com.zhang.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> selectAllRole() {
        return roleDao.selectAllRole();
    }
}
