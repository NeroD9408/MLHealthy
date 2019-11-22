package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.utils.SMSUtils;
import cn.itcast.utils.ValidateCodeUtils;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validate")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        //调用工具类获取随机的验证码
        String param = ValidateCodeUtils.generateValidateCode4String(4);
        try {
            //将随机验证码加入到redis中并设置过期时间
            /**
             * setex(存入redis中的key名称, 过期时间, 存入redis中的值)
             */
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 300, param);
            //发送验证码到用户的手机上
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, param);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //调用工具类获取随机的验证码
        String param = ValidateCodeUtils.generateValidateCode4String(6);
        try {
            //将随机验证码加入到redis中并设置过期时间
            /**
             * setex(存入redis中的key名称, 过期时间, 存入redis中的值)
             */
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 300, param);
            //发送验证码到用户的手机上
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, param);
            return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
