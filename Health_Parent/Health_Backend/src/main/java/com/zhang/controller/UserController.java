package com.zhang.controller;
//@author ZT 2019/7/16-19:04  

import com.alibaba.dubbo.config.annotation.Reference;
import com.zhang.constance.MessageConstant;
import com.zhang.constance.RedisConstant;
import com.zhang.entity.SetMeal;
import com.zhang.pojo.PageResult;
import com.zhang.pojo.QueryPageBean;
import com.zhang.pojo.Result;
import com.zhang.service.UserService;
import com.zhang.util.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = {"/user"})
public class UserController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private UserService userService;

    @RequestMapping(value = {"/getUsername"})
    public Result getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //获取框架的用户User对象，然后再获取用户的信息
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        String password = user.getPassword();
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
    }

    @RequestMapping(value = {"/getImg"})
    public Result getImg(String username) {
        try {
            String imgName = userService.getImg(username);
            return new Result(true, MessageConstant.GET_USER_IMG_SUCCESS, imgName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(true, MessageConstant.GET_USER_IMG_FAIL);
        }
    }

    /*
    * 用于查询每页的用户显示数据*/
    @RequestMapping(value = {"/selectUserPage"})
    @PreAuthorize(value = "hasAnyAuthority('USER_QUERY')")
    public PageResult selectUserPage(@RequestBody QueryPageBean queryPageBean) {
        return userService.selectUserPage(queryPageBean);
    }

    /*通过id查找用户*/
    @RequestMapping(value = {"/selectUser"})
    @PreAuthorize(value = "hasAnyAuthority('USER_QUERY')")
    public Result selectUserById(Integer id) {
        try {
            com.zhang.entity.User user = userService.selectUserById(id);
            return new Result(true, MessageConstant.QUERY_USER_SUCCESS, user);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_USER_FAIL);
        }
    }

    /*通过用户的id查找用户对应角色的id*/
    @RequestMapping(value = {"/selectRolesIdByUserId"})
    @PreAuthorize(value = "hasAnyAuthority('ROLE_QUERY')")
    public Result selectRolesByUserId(Integer id) {
        try {
            List<Integer> list = userService.selectRolesIdByUserId(id);
            return new Result(true, MessageConstant.QUERY_ROLE_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ROLE_FAIL);
        }
    }


    /*新增用户，同时将上传的图片路径保存到jedis中*/
    @RequestMapping(value = {"/add"})
    @PreAuthorize(value = "hasAnyAuthority('USER_ADD')")
    public Result addUser(@RequestBody com.zhang.entity.User user,
                          Integer[] roleIds) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        try {
            userService.add(user, roleIds);
            //为了防止没有上传图片而抛异常
            if (user.getImgName() != null && (!"".equals(user.getImgName()))) {
                jedisPool.getResource().sadd(RedisConstant.USER_PIC_DB_RESOURCES, user.getImgName());
            }
            return new Result(true, MessageConstant.ADD_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_USER_FAIL);
        }
    }

    /*更新用户数据，同时更新中间表*/
    @RequestMapping(value = {"/update"})
    @PreAuthorize(value = "hasAnyAuthority('USER_UPDATE')")
    public Result updateUser(@RequestBody com.zhang.entity.User user,
                             Integer[] roleIds) {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userService.update(user, roleIds);
            return new Result(true, MessageConstant.EDIT_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_USER_FAIL);
        }
    }

    /*根据用户id删除用户信息，同时删除中间表*/
    @RequestMapping(value = {"/delete"})
    @PreAuthorize(value = "hasAnyAuthority('USER_DELETE')")
    public Result delete(Integer id) {
        try {
            userService.delete(id);
            return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_USER_FAIL);
        }
    }

    /*上传用户图片用*/
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
            jedisPool.getResource().sadd(RedisConstant.USER_PIC_RESOURCES, filename);
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, filename);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }
}
