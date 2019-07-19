package com.zhang.jobs;
//@author ZT 2019/7/10-10:47  

import com.zhang.constance.RedisConstant;
import com.zhang.util.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg() {
        Set<String> set = jedisPool.getResource().
                sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        if (set != null) {
            for (String picName : set) {
                QiNiuUtils.deleteFileFromQiNiu(picName);
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, picName);
            }
        }
    }
}
