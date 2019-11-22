package cn.itcast.mapper;

import cn.itcast.domain.Permission;
import org.apache.ibatis.annotations.Select;

public interface PermissionMapper {

    //根据角色id查询角色对应的权限信息
    @Select("select p.* from t_permission p, t_role_permission rp where p.id = rp.permission_id and rp.role_id = #{rid}")
    Permission findPermissionByRid(Integer rid);
}
