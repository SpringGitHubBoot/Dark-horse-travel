package com.zhang.controller;
//@author ZT 2019/7/7-16:02  

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.constance.MessageConstant;
import com.zhang.entity.CheckGroup;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.pojo.Result;
import com.zhang.service.CheckGroupService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = {"/checkGroup"})
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping(value = {"/add"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKGROUP_ADD')")
    public Result addCheckGroup(@RequestBody CheckGroup checkGroup,
                                Integer[] checkitemIds) {
        try {
            checkGroupService.add(checkGroup, checkitemIds);
            return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
    }

    @PreAuthorize(value = "hasAnyAuthority('CHECKGROUP_QUERY')")
    @RequestMapping(value = {"/selectCheckGroupPage"})
    public PageResult selectCheckGroupPage(@RequestBody QueryPageBean queryPageBean) {

        return checkGroupService.selectCheckGroupPage(queryPageBean);

    }

    @PreAuthorize(value = "hasAnyAuthority('CHECKGROUP_QUERY')")
    @RequestMapping(value = {"/selectCheckGroup"})
    public Result selectCheckGroupById(Integer id) {

        try {
            CheckGroup checkGroup = checkGroupService.selectCheckGroupById(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_QUERY')")
    @RequestMapping(value = {"/selectCheckItemsByGroupId"})
    public Result selectCheckItemsByGroupId(Integer id) {

        try {
            List<Integer> list = checkGroupService.selectCheckItemsByGroupId(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    @RequestMapping(value = {"/update"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKGROUP_EDIT')")
    public Result updateCheckGroup(@RequestBody CheckGroup checkGroup,
                                   Integer[] checkitemIds) {
        try {
            checkGroupService.update(checkGroup, checkitemIds);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping(value = {"/delete"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKGROUP_DELETE')")
    public Result deleteCheckGroup(Integer id) {

        try {
            checkGroupService.delete(id);
            return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping(value = {"/selectAllCheckGroup"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKGROUP_QUERY')")
    public Result selectAllCheckGroup() {

        try {
            List<CheckGroup> checkGroups = checkGroupService.selectAllCheckGroup();
            return new Result(true, "", checkGroups);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
}
