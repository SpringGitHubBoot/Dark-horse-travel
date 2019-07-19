package com.zhang.controller;
//@author ZT 2019/7/14-15:34  

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.qiniu.util.Json;
import com.zhang.constance.MessageConstant;
import com.zhang.constance.RedisMessageConstant;
import com.zhang.pojo.Result;
import com.zhang.service.MemberService;
import com.zhang.util.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping(value = {"/member"})
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping(value = {"/login"})
    public Result login(HttpServletResponse response, @RequestBody Map map) {

        //登录的控制器只判断验证码是否一致，其他逻辑在service服务上完成
        //判断用户输入的验证码和redis中保存的是否一致
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");

        String redisValidateCode = jedisPool.getResource().
                get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (redisValidateCode == null || !redisValidateCode.equals(validateCode)) {
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

        //创建一个cookie用来记录用户信息
        Cookie cookie = new Cookie("MemberLoginTelephone", telephone);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);
        response.addCookie(cookie);
        //JSON.toJSON()
        jedisPool.getResource().setex(telephone, 60, telephone);
        //如果验证码没有问题，则访问服务来处理这次请求
        return memberService.login(telephone);
    }
}
