package com.zhang.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.constance.MessageConstant;
import com.zhang.entity.Permission;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.pojo.Result;
import com.zhang.service.PermissionService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    PermissionService permissionService;

    @RequestMapping(value = {"/add"})
//    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_ADD')")
    public Result addCheckItem(@RequestBody Permission permission) {
        try {
            //注意dubbo传递实体类数据则必须要序列化
            permissionService.addPermission(permission);
        } catch (Exception e) {
            //通过是否发生异常来得到返回给前端的数据，数据用Result来封装
            return new Result(false, MessageConstant.ADD_PERMISSION_FAIL);
        }
        return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
    }


    //获取当前页的权限数据
    @RequestMapping("/selectPageData")
    public PageResult selectPageData(@RequestBody QueryPageBean queryPageBean) {
        return permissionService.selectPageData(queryPageBean);
    }

    @RequestMapping(value = {"/queryPermission"})
//    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_QUERY')")
    public Result queryCheckItemById(Integer id) {
        Permission permission = permissionService.selectPermissionById(id);
        return new Result(permission);
    }


    @RequestMapping(value = {"/delete"})
//    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_DELETE')")
    public Result deleteCheckItem(Integer id) {

        try {
            //注意dubbo传递实体类数据则必须要序列化
            permissionService.deleteById(id);
        } catch (Exception e) {
            //通过是否发生异常来得到返回给前端的数据，数据用Result来封装
            return new Result(false, MessageConstant.DELETE_PERMISSION_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
    }


    @RequestMapping(value = {"/update"})
//    @PreAuthorize(value = "hasAnyAuthority('CHECKITEM_EDIT')")
    public Result updateCheckItem(@RequestBody Permission permission) {

        try {
            //注意dubbo传递实体类数据则必须要序列化
            permissionService.updatePermission(permission);
        } catch (Exception e) {
            //通过是否发生异常来得到返回给前端的数据，数据用Result来封装
            return new Result(false, MessageConstant.EDIT_PERMISSION_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
    }

}
