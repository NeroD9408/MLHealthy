package cn.itcast.mapper;

import cn.itcast.domain.Role;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface RoleMapper {

    //根据用户id查询用户对应的角色信息
    @Select("select r.* from t_role r, t_user_role ur where r.id = ur.role_id and ur.user_id = #{uid}")
    @Results({
            @Result(
                    property = "permissions",
                    javaType = Set.class,
                    column = "id",
                    many = @Many(select = "cn.itcast.mapper.PermissionMapper.findPermissionByRid")
            )
    })
    Role findRoleByUid(Integer uid);
}
