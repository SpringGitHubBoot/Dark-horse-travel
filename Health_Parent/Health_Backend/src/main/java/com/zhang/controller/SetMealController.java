package com.zhang.controller;
//@author ZT 2019/7/9-16:12  

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.constance.MessageConstant;
import com.zhang.constance.RedisConstant;
import com.zhang.entity.SetMeal;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.pojo.Result;
import com.zhang.service.SetMealService;
import com.zhang.util.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping(value = {"/setMeal"})
public class SetMealController {
    @Reference
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping(value = {"/upload"})
    public Result upload(@RequestParam(value = "imgFile") MultipartFile file) {

        String originalFilename = file.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index);

        String uuid = UUID.randomUUID().toString().replace("-", "");
        String filename = uuid + suffix;

        try {
            QiNiuUtils.upload2QiNiu(file.getBytes(), filename);
            //将上传的图片文件名存储到名为SETMEAL_PIC_RESOURCES对应的set集合中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, filename);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, filename);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping(value = {"/add"})
    @PreAuthorize(value = "hasAnyAuthority('SETMEAL_ADD')")
    public Result addSetMeal(@RequestBody SetMeal setMeal, Integer[] checkgroupIds) {
        try {
            setMealService.addSetMeal(setMeal, checkgroupIds);
            //将成功插入数据库的套餐数据中的图片名存入到redis名为SETMEAL_PIC_DB_RESOURCES的集合中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setMeal.getImg());
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping(value = {"/selectSetMealPage"})
    @PreAuthorize(value = "hasAnyAuthority('SETMEAL_QUERY')")
    public PageResult selectSetMealPage(@RequestBody QueryPageBean queryPageBean) {

        return setMealService.selectSetMealPage(queryPageBean);

    }

    @RequestMapping(value = {"/selectSetMealById"})
    @PreAuthorize(value = "hasAnyAuthority('SETMEAL_QUERY')")
    public Result selectSetMealById(Integer id) {

        try {
            SetMeal setMeal = setMealService.selectSetMealById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setMeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping(value = {"/selectCheckGroupIdBySetMealId"})
    @PreAuthorize(value = "hasAnyAuthority('CHECKGROUP_QUERY')")
    public Result selectCheckGroupIdBySetMealId(Integer id) {

        try {
            List<Integer> checkGroupIds = setMealService.selectCheckGroupIdBySetMealId(id);
            return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS, checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping(value = {"/update"})
    @PreAuthorize(value = "hasAnyAuthority('SETMEAL_EDIT')")
    public Result updateSetMeal(@RequestBody SetMeal setMeal, Integer[] checkgroupIds) {

        try {
            setMealService.updateSetMeal(setMeal, checkgroupIds);
            return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }

    @RequestMapping(value = {"/delete"})
    @PreAuthorize(value = "hasAnyAuthority('SETMEAL_DELETE')")
    public Result deleteSetMeal(@RequestBody SetMeal setMeal) {

        try {
            setMealService.deleteSetMeal(setMeal);
            return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
    }



    @RequestMapping("/getSetMeaList")
    @PreAuthorize(value = "hasAnyAuthority('SETMEAL_QUERY')")
    public Result getSetMeaList(@RequestBody QueryPageBean queryPageBean){
        try{
           PageResult pageResult = setMealService.getSetMeaList(queryPageBean);
           if(pageResult==null || "".equals(pageResult)){
               return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
           }
           return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, pageResult);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }

}