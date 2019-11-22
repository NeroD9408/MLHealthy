package cn.itcast.mapper;

import cn.itcast.domain.UserInfo;
import org.apache.ibatis.annotations.*;

import java.util.Set;

public interface UserInfoMapper {

    //根据用户名查询用户信息，并且关联查询角色信息和权限信息
    @Select("select * from t_user where username = #{username}")
    @Results({
            @Result(
                    property = "roles",
                    javaType = Set.class,
                    column = "id",
                    many = @Many(select = "cn.itcast.mapper.RoleMapper.findRoleByUid")
            )
    })
    UserInfo findUserInfoByUsername(String username);

    //通过用户名密码确认用户信息
    @Select("select * from t_user where username = #{username}")
    UserInfo findUserInfo(String username);

    //修改登录用户的头像地址
    @Update("update t_user set headimg = #{headimg} where username = #{username}")
    void updateUserHead(@Param("headimg") String headimg, @Param("username") String loginUsername);
}
