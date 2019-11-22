package cn.itcast.jobs;

import cn.itcast.constant.RedisConstant;
import cn.itcast.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;
    public void clearImg(){
        //获取两个集合的差值集合
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        //遍历差值集合，使用工具类删除云存储中的垃圾图片，然后删除上传集合中的图片路径数据
        if (set != null && set.size() > 0) {
            for (String picName : set) {
                jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES, picName);
                QiniuUtils.deleteFileFromQiniu(picName);
            }
        }
    }
}
