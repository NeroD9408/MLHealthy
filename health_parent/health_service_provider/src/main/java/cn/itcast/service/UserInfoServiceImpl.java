package cn.itcast.service;

import cn.itcast.domain.UserInfo;
import cn.itcast.mapper.UserInfoMapper;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = UserInfoService.class)
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    //根据用户名查询用户信息
    @Override
    public UserInfo findUserInfoByUsername(String username) {
        //查询出来的用户信息需要包含角色信息和权限信息
        UserInfo userInfo = null;
        try {
            userInfo = userInfoMapper.findUserInfoByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    //通过用户名密码确认用户信息
    @Override
    public UserInfo findUserInfo(String username) {
        return userInfoMapper.findUserInfo(username);
    }

    //修改当前登录用户的头像地址
    @Override
    public void updateUserHead(String headimg, String loginUsername) {
        userInfoMapper.updateUserHead(headimg, loginUsername);
    }
}
