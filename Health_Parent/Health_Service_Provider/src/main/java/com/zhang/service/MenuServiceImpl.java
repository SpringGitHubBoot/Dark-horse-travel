package com.zhang.service;
//@author ZT 2019/7/17-10:43  

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhang.dao.MenuDao;
import com.zhang.dao.RoleDao;
import com.zhang.entity.Menu;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MenuService.class)
@Transactional
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private RoleDao roleDao;

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

    @Override
    public PageResult selectMenuPageData(QueryPageBean pageRequestData) {
        Integer currentPage = pageRequestData.getCurrentPage();
        String keyWord = pageRequestData.getQueryString();
        Integer pageSize = pageRequestData.getPageSize();

        PageHelper.startPage(currentPage, pageSize);
        Page<Menu> menuPage = menuDao.selectMenuPageData(keyWord);
        return new PageResult(menuPage.getTotal(), menuPage.getResult());
    }

    @Override
    public void addMenu(Menu menu) throws Exception {
        //获取用户输入的菜单等级
        int level = menu.getLevel();
        //获取用户输入的路径
        String path = menu.getPath();
        //用路径查询菜单
        Menu thePathMenu = menuDao.findMenuByPath(path);

        //说明输入的路径已经有菜单了，数据有误，本次添加失败
        if (thePathMenu != null) {
            throw new RuntimeException();
        }

        //用户输入了一个一级路径
        if (path.length() == 1) {
            if (level == 2) {
                throw new RuntimeException();
            }
            //获取最大的turn值
            int maxTurnNumber = menuDao.getMaxTurnNumber();
            //新菜单的turn是最大值+1
            menu.setTurn(maxTurnNumber + 1);
            //判断一下这个一级菜单有没有图标
            if (menu.getDescription() == null) {
                //给它一个图标
                menu.setDescription("fa-heartbeat");
            }
            //添加菜单并返回
            menuDao.addMenu(menu);
            int menuId = menu.getId();
            insertIntoRoleAndMenu(menuId);
            return;
        }
        //用户输入了一个二级路径
        else {
            //二级路径对应第一等级，说明输入的数据有误，本次添加失败
            if (level == 1) {
                throw new RuntimeException();
            } else {
                //获取这个路径对应的一级路径
                char charOfLevel1 = path.charAt(1);
                String mainPathNum = Character.toString(charOfLevel1);
                Menu menuByMainPathNum = menuDao.findMenuByPath(mainPathNum);
                //说明输入的路径是一个二级菜单却没有一级菜单，数据有误，本次添加失败
                if (menuByMainPathNum == null) {
                    throw new RuntimeException();
                } else {
                    //获取这个二级菜单的最后一位数字
                    int charAtLastNum = Integer.parseInt(path.charAt(path.length() - 1) + "");
                    //去掉path路径字符串的最后一个字符，得到如/2-
                    String pathHead = path.substring(0, path.length() - 1);
                    //如果最后一个数字不是1
                    if (charAtLastNum > 1) {
                        //遍历，直到找到一个最小的最后一位数字，要它的turn值
                        for (int i = charAtLastNum; i >= 1; i--) {
                            //根据拼接的路径查找菜单
                            Menu menuByPath = menuDao.findMenuByPath(pathHead + i);
                            if (menuByPath != null) {
                                //得到一个菜单，说明新插入的数据的turn值应刚好比它大 1
                                int turn = menuByPath.getTurn();
                                menu.setTurn(turn + 1);
                                //更新表中的turn值，比这个菜单turn大的turn值都要+1
                                menuDao.updateMenuTurnsAfterInsert(turn);
                                //添加新菜单并返回
                                menuDao.addMenu(menu);
                                int menuId = menu.getId();
                                insertIntoRoleAndMenu(menuId);
                                return;
                            }
                        }
                    }
                    //来到这里说明path的最后一位是1
                    else {
                        //找打这个path对应的一级路径
                        String mainPath = path.charAt(1) + "";
                        //获得这个一级菜单
                        Menu menuByPath = menuDao.findMenuByPath(mainPath);
                        //得到一级菜单的turn值，新菜单的turn刚好比它大 1
                        int turn = menuByPath.getTurn();
                        menu.setTurn(turn + 1);
                        //同样更新然后添加菜单并返回
                        menuDao.updateMenuTurnsAfterInsert(turn);
                        menuDao.addMenu(menu);
                        int menuId = menu.getId();
                        insertIntoRoleAndMenu(menuId);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public Menu queryMenuById(Integer id) {
        return menuDao.queryMenuById(id);
    }

    @Override
    public void updateMenu(Menu menu) {
        int level = menu.getLevel();
        String description = menu.getDescription();
        if (level == 1 && (description == null || "".equals(description))) {
            //给它一个图标
            menu.setDescription("fa-heartbeat");
        }
        menuDao.updateMenu(menu);
    }

    @Override
    public void deleteMenu(Integer id) throws Exception {
        Menu menu = menuDao.queryMenuById(id);
        int level = menu.getLevel();
        String path = menu.getPath();
        int turn = menu.getTurn();
        List<Integer> roleIds = roleDao.getRoleIdsAboutMenuId(id);
        if (level == 2) {
            menuDao.updateMenuTurnsAfterDelete(turn);
            if (roleIds != null && roleIds.size() > 0) {
                this.deleteFromRoleAndMenuByIds(roleIds, id);
                menuDao.deleteMenuById(id);
            } else {
                menuDao.deleteMenuById(id);
            }
        } else {
            String childPath = "/" + path;
            List<Menu> menuList = menuDao.findMenuListByPartPath(childPath);
            if (menuList.size() > 0 && menuList != null) {
                throw new RuntimeException();
            } else {

                menuDao.updateMenuTurnsAfterDelete(turn);
                if (roleIds != null && roleIds.size() > 0) {
                    this.deleteFromRoleAndMenuByIds(roleIds, id);
                    menuDao.deleteMenuById(id);
                } else {
                    menuDao.deleteMenuById(id);
                }
            }
        }
    }

    //删除角色和菜单中间表的指定记录
    public void deleteFromRoleAndMenuByIds(List<Integer> roleIds, int menuId) {
        Map map = new HashMap<>();
        map.put("menuId", menuId);
        for (Integer roleId : roleIds) {
            map.put("roleId", roleId);
            roleDao.deleteFromRoleAndMenuByIds(map);
        }
    }

    //向角色和菜单中间表插入数据
    public void insertIntoRoleAndMenu(int menuId) {
        Map map = new HashMap<>();
        map.put("roleId", 1);
        map.put("menuId", menuId);
        roleDao.insertIntoRoleAndMenu(map);
    }
}
