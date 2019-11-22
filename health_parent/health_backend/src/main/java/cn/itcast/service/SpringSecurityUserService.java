package cn.itcast.service;

import cn.itcast.domain.Permission;
import cn.itcast.domain.Role;
import cn.itcast.domain.UserInfo;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {

    @Reference
    private UserInfoService userInfoService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //通过用户名查询数据库获取用户信息，而且需要一并查询用户的角色和权限信息
        UserInfo userInfo = userInfoService.findUserInfoByUsername(username);
        List<GrantedAuthority> list = new ArrayList<>();
        Set<Role> roles = userInfo.getRoles();
        //将角色和权限全部添加到集合中去
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                //将角色信息添加到集合中去
                list.add(new SimpleGrantedAuthority(role.getKeyword()));
                //接着遍历权限信息集合
                Set<Permission> permissions = role.getPermissions();
                if (permissions != null && permissions.size() > 0) {
                    for (Permission permission : permissions) {
                        list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                    }
                }
            }
        }
        User user = new User(username, userInfo.getPassword(), list);
        return user;
    }
}
