package cn.itcast.service;

import cn.itcast.domain.UserInfo;

public interface UserInfoService {

    //根据用户名查询用户信息
    UserInfo findUserInfoByUsername(String username);

    //通过用户名密码确认用户信息
    UserInfo findUserInfo(String username);

    //修改登录用户的头像地址
    void updateUserHead(String headimg, String loginUsername);
}
