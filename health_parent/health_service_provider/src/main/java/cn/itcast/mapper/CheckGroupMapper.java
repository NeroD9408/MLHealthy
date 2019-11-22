package cn.itcast.mapper;

import cn.itcast.domain.CheckGroup;
import cn.itcast.domain.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CheckGroupMapper {

    //新增检查项
    @Insert("insert into t_checkgroup values(null, #{code}, #{name}, #{helpCode}, #{sex}, #{remark}, #{attention})")
    @Options(useGeneratedKeys=true, keyColumn="id", keyProperty = "id")
    void addCheckGroup(CheckGroup checkGroup);

    //添加检查组合检查项的对应
    @Insert("insert into t_checkgroup_checkitem values(#{gid}, #{id})")
    void addCheckGroupAndItem(@Param("gid") Integer gid, @Param("id") String id);

    //根据查询条件进行查询数据
    @Select({
            "<script>" +
            "select * from t_checkgroup where 1=1 " +
                "<if test='condition != null and condition.length &gt; 0'>" +
                    "and code like '%${condition}%' or helpCode like '%${condition}%' or name like '%${condition}%'" +
                "</if>" +
            "</script>"
    })
    Page<CheckGroup> findCondition(@Param("condition") String queryString);

    //根据id进行关联关系的清除
    @Delete("delete from t_checkgroup_checkitem where checkgroup_id = #{id}")
    void clearByGid(String id);

    //根据id进行数据删除
    @Delete("delete from t_checkgroup where id = #{id}")
    void deleteById(String id);

    //根据id进行查询
    @Select("select * from t_checkgroup where id = #{id}")
    @Results({
            @Result(
                    property = "checkItems",
                    javaType = List.class,
                    column = "id",
                    many = @Many(select = "cn.itcast.mapper.CheckItemMapper.findByGid")
            )
    })
    CheckGroup findById(String id);

    //对检查项的基本数据进行修改
    @Update("update t_checkgroup set code = #{code}, name = #{name}, helpCode = #{helpCode}," +
            " sex = #{sex}, remark = #{remark}, attention = #{attention} where id = #{id}")
    void updateCheckGroup(CheckGroup checkGroup);

    //查询所有检查项
    @Select("select * from t_checkgroup")
    List<CheckGroup> findAll();

    //根据套餐的id查询所有的检查项信息
    @Select("select * from t_checkgroup where id in(select checkgroup_id from t_setmeal_checkgroup where setmeal_id = #{mid})")
    @Results({
            @Result(
                    property = "checkItems",
                    javaType = List.class,
                    column = "id",
                    many = @Many(select = "cn.itcast.mapper.CheckItemMapper.findByGid")
            )
    })
    List<CheckGroup> findSetMealCheckGroups(String mid);
}