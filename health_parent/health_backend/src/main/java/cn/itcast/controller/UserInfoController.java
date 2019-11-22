package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.domain.UserInfo;
import cn.itcast.entity.Result;
import cn.itcast.service.UserInfoService;
import cn.itcast.utils.HandleImgUtils;
import cn.itcast.utils.QiniuHeadImgUtils;
import cn.itcast.utils.UuidUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserInfoController {

    private String loginUsername = "";

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private UserInfoService userInfoService;

    @RequestMapping("/findLoginUser")
    public Result findLoginUser() {
        try {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            loginUsername = user.getUsername();
            UserInfo loginUser = userInfoService.findUserInfo(loginUsername);
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, loginUser);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }

    @RequestMapping("/updateHeadImg")
    public Result updateHeadImg(String imgFile, HttpServletRequest request) {
        //获取存储的真实路径
        String realPath = request.getSession().getServletContext().getRealPath("/");
        System.out.println(realPath);
        String filePath = realPath + UuidUtil.getUuid() + ".jpg";
        //将文件保存到根目录
        HandleImgUtils.handleImg(imgFile, filePath);
        byte[] bytes = HandleImgUtils.handleImg(imgFile);
        String imgName = UuidUtil.getUuid() + ".jpg";
        //调用工具类将图片保存到云端
        //直接使用字符串获取bytes
        QiniuHeadImgUtils.upload2Qiniu(bytes, imgName);
        //修改当前登录账号的头像信息
        userInfoService.updateUserHead("http://q1bny4qv1.bkt.clouddn.com/" + imgName, loginUsername);

        return new Result(true, "成功");
    }
}
