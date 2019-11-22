package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.domain.Order;
import cn.itcast.entity.Result;
import cn.itcast.service.OrderService;
import cn.itcast.utils.SMSUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 用来处理预约信息的controller
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map) {
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //首先需要先对验证码进行比较，查看用户输入的验证码是否正确
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        if (validateCode != null && validateCode.equals(validateCodeInRedis)) {
            //说明用户输入的验证码是正确的，可以进行service的调用进行业务操作
            //设置预约的类型
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            //调用service进行预约
            Result result = null;
            try {
                result = orderService.order(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result != null && result.isFlag()) {
                //预约成功，调用工具类来给用户发送预约成功的信息
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, (String) map.get("orderDate"));
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
            return result;
        } else {
            //说明用户输入的验证码是错误的，返回错误信息
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id) {
        try {
            Map<String, Object> response = orderService.getOrderDetail(id);
            return new Result(true, MessageConstant.QUERY_ORDER_SUCCESS, response);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
