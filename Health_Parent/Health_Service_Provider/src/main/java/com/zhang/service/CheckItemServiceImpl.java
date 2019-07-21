package com.zhang.service;
//@author ZT 2019/7/6-16:10  

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhang.dao.CheckGroupDao;
import com.zhang.dao.CheckItemDao;
import com.zhang.entity.CheckGroup;
import com.zhang.entity.CheckItem;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Autowired
    private JedisPool jedisPool;


    @Override
    public void addCheckItem(CheckItem checkItem) {
        checkItemDao.addCheckItem(checkItem);
    }

    @Override
    public PageResult selectPageData(QueryPageBean queryPageBean) {

        //使用分页插件，传入两个参数当前页和每页记录条数
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //调用持久层方法，注意用Page来接收
        String queryString = queryPageBean.getQueryString();
        if (queryString == null) {
            queryString = "";
        }
        Page<CheckItem> page = checkItemDao.selectPageData("%" + queryString + "%");

        return new PageResult(page.getTotal(), page.getResult());

    }

    @Override
    public void deleteById(Integer id) {
        //删除记录，需要先查询该记录是否和其他表关联了，在相关中间表中查询即可
        int line = checkItemDao.selectCheckItemIDInCC(id);
        //查询的条数大于零，说明该记录与其他表相关
        if (line > 0) {
            throw new RuntimeException();
            //查询的条数小于零，说明该记录与其他表无关，可以删除
        } else {
            checkItemDao.deleteCheckItemById(id);
        }
    }

    @Override
    public CheckItem selectCheckItemById(Integer id) {
        return checkItemDao.selectCheckItemById(id);
    }

    @Override
    public void updateCheckItem(CheckItem checkItem) {
        checkItemDao.updateCheckItem(checkItem);
        //获取修改的检查项的id
        int checkItemId = checkItem.getId();
        //通过检查项id获取它关联所有的检查组id
        List<Integer> ids = checkItemDao.findCheckGroupIds(checkItemId);

        //遍历检查组id
        for (Integer id : ids) {
            //找到每一个检查组id关联的全部套餐id
            List<Integer> setMealIds = checkGroupDao.findSetMealIds(id);
            //清空这些已经变动的套餐详情信息
            this.resetRedisSetMealById(setMealIds);
        }
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
    public List<CheckItem> selectAllCheckItem() {
        return checkItemDao.selectAllCheckItem();
    }
}
