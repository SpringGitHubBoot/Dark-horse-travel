package com.zhang.service;
//@author ZT 2019/7/9-17:15  

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhang.dao.SetMealDao;
import com.zhang.entity.SetMeal;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.util.DateUtils;
import com.zhang.util.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.*;

import static com.sun.tools.doclint.Entity.and;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void addSetMeal(SetMeal setMeal, Integer[] checkGroupIds) {
        //先在套餐表中添加一条套餐记录
        setMealDao.addSetMeal(setMeal);
        //获取刚添加的套餐记录的主键id值
        int setMealId = setMeal.getId();
        //添加的套餐记录可能和多条检查组记录有关联，将关联信息添加到关系表中
        this.addRelationOfSetMealAndCheckGroup(setMealId, checkGroupIds);
        resetRedisSetMeal();

    }

    @Override
    public PageResult selectSetMealPage(QueryPageBean queryPageBean) {

        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
//        String keyword = "%" + queryString + "%";

        PageHelper.startPage(currentPage, pageSize);
        Page<SetMeal> setMealPage = setMealDao.selectSetMealPage(queryString);
        return new PageResult(setMealPage.getTotal(), setMealPage.getResult());
    }

    @Override
    public SetMeal selectSetMealById(Integer id) {
        return setMealDao.selectSetMealById(id);
    }

    @Override
    public List<Integer> selectCheckGroupIdBySetMealId(Integer id) {
        return setMealDao.selectCheckGroupIdBySetMealId(id);
    }

    @Override
    public void updateSetMeal(SetMeal setMeal, Integer[] checkgroupIds) {

        setMealDao.deleteSetMealIdFromMid(setMeal.getId());
        this.addRelationOfSetMealAndCheckGroup(setMeal.getId(), checkgroupIds);
        setMealDao.updateSetMeal(setMeal);
        resetRedisSetMeal();
        int setMealId = setMeal.getId();
        resetRedisSetMealById(setMealId);
    }

    @Override
    public void deleteSetMeal(SetMeal setMeal) {
        setMealDao.deleteRelationOfSetMealAndCheckGroup(setMeal.getId());
        //必须主动删除七牛云中的垃圾图片
        //因为删除mysql数据库记录时不能将redis数据库图片名删除，导致七牛云中的垃圾图片一直存在
        //最好将Jedis配置文件引入到服务提供者中，这样删除垃圾图片的同时也能删除redis数据库中无用数据
        String imgName = setMeal.getImg();
        QiNiuUtils.deleteFileFromQiNiu(imgName);
        setMealDao.deleteSetMealById(setMeal.getId());
        resetRedisSetMeal();
    }

    //重置redis的setMealList集合的方法
    public void resetRedisSetMeal() {
        //增删改套餐后判断redis中是否存在套餐列表
        String setMealList = jedisPool.getResource().get("setMealList");
        //如果存在
        if (setMealList != null) {
            //使setMealList的值为""，查询套餐详情时候就需要重新查询数据库并重新给redis赋值
            jedisPool.getResource().set("setMealList","");
        }
    }

    //重置redis的某个套餐id对应的套餐详情数据
    public void resetRedisSetMealById(Integer id) {
        //修改套餐、增删改检查组或者增删改检查项后，判断redis中是否存在某个id对应的套餐详情
        String idStr = id.toString();
        String setMealDetail = jedisPool.getResource().get(idStr);
        //如果存在
        if (setMealDetail != null) {
            //使idStr的值为""，查询套餐详情时候就需要重新查询数据库并重新给redis赋值
            jedisPool.getResource().set(idStr,"");
        }
    }

    @Override
    public List<SetMeal> selectAllSetMeal() {
        return setMealDao.selectAllSetMeal();
    }

    @Override
    public SetMeal findById(Integer id) {
        return setMealDao.findById(id);
    }

    @Override
    public Map getSetMealReport() {

        List<Map> setmealCount = setMealDao.selectSetMealReport();
        if (setmealCount != null && setmealCount.size() > 0) {
            Map<String, Object> data = new HashMap<>();
            ArrayList<String> setmealNames = new ArrayList<>();
            for (Map map : setmealCount) {
                String name = (String) map.get("name");
                setmealNames.add(name);
            }
            data.put("setmealCount", setmealCount);
            data.put("setmealNames", setmealNames);
            return data;
        }
        return null;
    }

    //获取预约列表信息
    @Override
    public PageResult getSetMeaList(QueryPageBean queryPageBean) throws Exception {

        Integer currentPage = queryPageBean.getCurrentPage();

        Integer pageSize = queryPageBean.getPageSize();

        String queryString = queryPageBean.getQueryString();

        if(queryString==null || "".equals(queryString)){
            queryString = "";
        }



        PageHelper.startPage(currentPage, pageSize);

        Page<Map> page = setMealDao.getSetMeaList("%"+queryString+"%");

        //获得当前页数据列表
        List<Map> dataList = page.getResult();
        //设置时间格式
        for (Map map : dataList) {
            Date orderTime = (Date) map.get("OrderTime");
            String time = DateUtils.parseDate2String(orderTime, "yyyy-MM-dd");
            map.put("OrderTime",time);
        }



        PageResult pageResult = new PageResult(page.getTotal(),dataList);


        return pageResult;
    }

    public void addRelationOfSetMealAndCheckGroup(Integer setMealId, Integer[] checkGroupIds) {

        HashMap<String, Integer> map = new HashMap<>();

        map.put("setMealId", setMealId);

        for (Integer checkGroupId : checkGroupIds) {
            map.put("checkGroupId", checkGroupId);
            setMealDao.addRelationOfSetMealAndCheckGroup(map);
        }
    }
}
