package com.zhang.controller;
//@author ZT 2019/7/6-15:58  

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.service.CheckItemService;
import com.zhang.constance.MessageConstant;
import com.zhang.entity.CheckItem;
import com.zhang.pojo.Result;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = {"/checkItem"})
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    @RequestMapping(value = {"/add"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_ADD')")
    public Result addCheckItem(@RequestBody CheckItem checkItem) {

        try {
            //注意dubbo传递实体类数据则必须要序列化
            checkItemService.addCheckItem(checkItem);
        } catch (Exception e) {
            //通过是否发生异常来得到返回给前端的数据，数据用Result来封装
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @RequestMapping(value = {"/selectPageData"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_QUERY')")
    public PageResult selectCheckItemPageData(@RequestBody QueryPageBean queryPageBean) {
        return checkItemService.selectPageData(queryPageBean);
    }

    @RequestMapping(value = {"/delete"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_DELETE')")
    public Result deleteCheckItem(Integer id) {

        try {
            //注意dubbo传递实体类数据则必须要序列化
            checkItemService.deleteById(id);
        } catch (Exception e) {
            //通过是否发生异常来得到返回给前端的数据，数据用Result来封装
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @RequestMapping(value = {"/queryCheckItem"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_QUERY')")
    public Result queryCheckItemById(Integer id) {

        CheckItem checkItem = checkItemService.selectCheckItemById(id);

        return new Result(checkItem);
    }

    @RequestMapping(value = {"/update"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_EDIT')")
    public Result updateCheckItem(@RequestBody CheckItem checkItem) {

        try {
            //注意dubbo传递实体类数据则必须要序列化
            checkItemService.updateCheckItem(checkItem);
        } catch (Exception e) {
            //通过是否发生异常来得到返回给前端的数据，数据用Result来封装
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS);
    }

    @RequestMapping(value = {"/selectAllCheckItem"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_QUERY')")
    public Result selectAllCheckItem() {

        try {
            //注意dubbo传递实体类数据则必须要序列化
            List<CheckItem> checkItemList = checkItemService.selectAllCheckItem();
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemList);
        } catch (Exception e) {
            //通过是否发生异常来得到返回给前端的数据，数据用Result来封装
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
}
