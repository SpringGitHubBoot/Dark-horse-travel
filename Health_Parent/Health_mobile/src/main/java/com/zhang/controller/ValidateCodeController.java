package com.zhang.controller;
//@author ZT 2019/7/12-20:55  


import com.zhang.constance.MessageConstant;
import com.zhang.constance.RedisMessageConstant;
import com.zhang.pojo.Result;
import com.zhang.util.SMSUtils;
import com.zhang.util.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping(value = {"/validateCode"})
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping(value = {"/send4Order"})
    public Result send4Order(String telephone) {
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
        } catch (Exception e) {
            e.printStackTrace();
            new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

        //用用户手机号码+为用户所发验证码的类型来作为验证码存储到redis中的键，起到唯一标识作用
        jedisPool.getResource().setex(
                telephone + RedisMessageConstant.SENDTYPE_ORDER, 5 * 60, code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping(value = {"/send4Login"})
    public Result send4Login(String telephone) {
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
        } catch (Exception e) {
            e.printStackTrace();
            new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }

        //用用户手机号码+为用户所发验证码的类型来作为验证码存储到redis中的键，起到唯一标识作用
        jedisPool.getResource().setex(
                telephone + RedisMessageConstant.SENDTYPE_LOGIN, 5 * 60, code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
