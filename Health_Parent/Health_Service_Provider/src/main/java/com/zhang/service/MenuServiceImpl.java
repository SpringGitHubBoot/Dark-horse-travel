package com.zhang.service;
//@author ZT 2019/7/17-10:43  

import com.alibaba.dubbo.config.annotation.Service;
import com.zhang.dao.MenuDao;
import com.zhang.entity.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public List<Menu> getMenuByUsername(String username) {
        //新建一个集合用来返回最终的集合数据
        ArrayList<Menu> list = new ArrayList<>();
        //定义子菜单集合
        ArrayList<Menu> children = new ArrayList<>();
        //通过调用持久层可以得到该用户的角色对应的所有菜单，包括1级和2级的
        List<Menu> menusByUsername = menuDao.getMenusByUsername(username);
        //菜单集合不为空才进入下面的代码
        if (menusByUsername != null && menusByUsername.size() > 0) {

            //定义父菜单
            Menu parentMenu = null;
            for (int i = 0; i < menusByUsername.size(); i++) {
                Menu menu = menusByUsername.get(i);
                if (menu.getLevel() == 1) {
                    if (parentMenu != null) {
                        list.add(parentMenu);
                        children = new ArrayList<>();
                    }
                    parentMenu = menu;
                    parentMenu.setChildren(children);
                } else {
                    parentMenu.getChildren().add(menu);
                }
            }
            //最后有一个父菜单没有添加到集合，最后要将它添加到集合
            list.add(parentMenu);
        }
        return list;
    }
}
