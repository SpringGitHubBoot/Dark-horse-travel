package com.zhang.service;
//@author ZT 2019/7/7-16:09  

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhang.dao.CheckGroupDao;
import com.zhang.entity.CheckGroup;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Autowired
    private JedisPool jedisPool;


    @Override
    //添加检查组的同时，需要在关联表中建立检查组合检查项的关系
    //也就是在中间表中添加多个联合主键
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {

        checkGroupDao.add(checkGroup);
        int checkGroupId = checkGroup.getId();
        this.insertTableCheckGroupAndCheckItem(checkGroupId, checkItemIds);
    }

    @Override
    public PageResult selectCheckGroupPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        String queryString = queryPageBean.getQueryString();
        Integer pageSize = queryPageBean.getPageSize();
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupDao.selectCheckGroupPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public CheckGroup selectCheckGroupById(Integer id) {
        return checkGroupDao.selectCheckGroupById(id);
    }

    @Override
    public List<Integer> selectCheckItemsByGroupId(Integer id) {
        return checkGroupDao.selectCheckItemsByGroupId(id);
    }

    @Override
    //修改检查组数据，有可能该组关联的检查项变化，因此做如下操作：
    //1.在关系表中删除该检查组关联的所有检查项
    //2.根据新编辑的数据，将该组关联的检查项数据插入到关系表中
    public void update(CheckGroup checkGroup, Integer[] checkItemIds) {
        //更新检查组表中的数据
        checkGroupDao.updateCheckGourp(checkGroup);
        //获取检查组的id
        int checkGroupId = checkGroup.getId();
        //通过检查组的id删除所有与检查项的关联数据
        checkGroupDao.deleteFromTableGroupAndItemByCheckGroupID(checkGroupId);
        //将检查组对应新的检查项插入到关系表中
        this.insertTableCheckGroupAndCheckItem(checkGroupId, checkItemIds);
        List<Integer> ids = checkGroupDao.findSetMealIds(checkGroupId);

        //将检查组id对应的所有套餐redis详情数据都清空
        this.resetRedisSetMealById(ids);
    }

    //修改某个检查组后，重置redis的该组对应的所有套餐id对应的套餐详情数据
    public void resetRedisSetMealById(List<Integer> ids) {
        //修改套餐、增删改检查组或者增删改检查项后，判断redis中是否存在某个id对应的套餐详情
        for (Integer id : ids) {
            String idStr = id.toString();
            String setMealDetail = jedisPool.getResource().get(idStr);
            //如果存在
            if (setMealDetail != null) {
                //使idStr的值为""，查询套餐详情时候就需要重新查询数据库并重新给redis赋值
                jedisPool.getResource().set(idStr, "");
            }
        }
    }

    @Override
    public void delete(Integer id) {
        checkGroupDao.deleteCheckGroupIdInAssociationTable(id);
        checkGroupDao.deleteCheckGroupById(id);
    }

    @Override
    public List<CheckGroup> selectAllCheckGroup() {
        return checkGroupDao.selectAllCheckGroup();
    }

    //抽离的方法，用来在t_checkGroup_checkItem表中插入值
    public void insertTableCheckGroupAndCheckItem(Integer checkGroupId, Integer[] checkItemIds) {

        HashMap<String, Integer> map = new HashMap<>();
        map.put("checkGroupId", checkGroupId);

        for (Integer checkItemId : checkItemIds) {
            map.put("checkItemId", checkItemId);
            checkGroupDao.insertTableCheckGroupAndCheckItem(map);
        }
    }
}
