package com.zhang.service;
//@author ZT 2019/7/6-16:10  

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zhang.dao.CheckItemDao;
import com.zhang.entity.CheckItem;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.pojo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public void addCheckItem(CheckItem checkItem) {
        checkItemDao.addCheckItem(checkItem);
    }

    @Override
    public PageResult selectPageData(QueryPageBean queryPageBean) {

        //使用分页插件，传入两个参数当前页和每页记录条数
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        //调用持久层方法，注意用Page来接收
        Page<CheckItem> page = checkItemDao.selectPageData(queryPageBean.getQueryString());

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
    }

    @Override
    public List<CheckItem> selectAllCheckItem() {
        return checkItemDao.selectAllCheckItem();
    }
}
