package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.domain.Member;
import cn.itcast.entity.Result;
import cn.itcast.service.MemberService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/fastLogin")
    public Result fastLogin(HttpServletResponse response, @RequestBody Map<String, Object> map) throws JsonProcessingException {
        //获取用户输入的手机号
        String telephone = (String) map.get("telephone");
        //获取用户输入的验证码
        String validateCode = (String) map.get("validateCode");
        //获取redis中的验证码
        String validateCodeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        //比较验证码是否正确
        if (validateCode != null && validateCode.equals(validateCodeInRedis)) {
            //验证码验证成功
            Member member = memberService.fastLogin(telephone);
            if (member == null) {
                //没有查询到会员信息，那么就自动注册
                Member registerMember = new Member();
                registerMember.setPhoneNumber(telephone);
                registerMember.setRegTime(new Date());
                memberService.register(registerMember);
                //注册结束之后自动登录
                member = memberService.fastLogin(telephone);
            }

            //将用户手机号存入到cookie
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 30);
            response.addCookie(cookie);

            //将用户信息存入redis
            jedisPool.getResource().setex(telephone, 60 * 30, new ObjectMapper().writeValueAsString(member));
            //设置登录成功的信息
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        } else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
