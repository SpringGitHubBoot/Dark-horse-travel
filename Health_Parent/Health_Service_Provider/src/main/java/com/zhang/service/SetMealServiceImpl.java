package com.zhang.service;
//@author ZT 2019/7/9-17:15  

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhang.dao.SetMealDao;
import com.zhang.entity.SetMeal;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.util.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealDao setMealDao;

    @Override
    public void addSetMeal(SetMeal setMeal, Integer[] checkGroupIds) {
        //先在套餐表中添加一条套餐记录
        setMealDao.addSetMeal(setMeal);
        //获取刚添加的套餐记录的主键id值
        int setMealId = setMeal.getId();
        //添加的套餐记录可能和多条检查组记录有关联，将关联信息添加到关系表中
        this.addRelationOfSetMealAndCheckGroup(setMealId, checkGroupIds);
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

    public void addRelationOfSetMealAndCheckGroup(Integer setMealId, Integer[] checkGroupIds) {

        HashMap<String, Integer> map = new HashMap<>();

        map.put("setMealId", setMealId);

        for (Integer checkGroupId : checkGroupIds) {
            map.put("checkGroupId", checkGroupId);
            setMealDao.addRelationOfSetMealAndCheckGroup(map);
        }
    }
}
