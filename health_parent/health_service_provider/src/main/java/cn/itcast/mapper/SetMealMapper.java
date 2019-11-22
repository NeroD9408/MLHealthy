package cn.itcast.mapper;

import cn.itcast.domain.Setmeal;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import javax.ws.rs.DELETE;
import java.util.List;

public interface SetMealMapper {

    //添加套餐信息
    @Insert("insert into t_setmeal values(null, #{name}, #{code}, #{helpCode}, #{sex}, #{age}, #{price}, #{remark}, #{attention}, #{img})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "id")
    void addMeal(Setmeal setmeal);

    //向关系表添加套餐与检查项之间的关系
    @Insert("insert into t_setmeal_checkgroup values(#{mid}, #{gid})")
    void addMealCheckGroup(@Param("mid") Integer mid, @Param("gid") String gid);

    @Select("<script>" +
            "select * from t_setmeal where 1=1 " +
                "<if test='condition != null and condition.length &gt; 0'>" +
                    "and code like '%${condition}%' or name like '%${condition}%' or helpCode like '%${condition}%'" +
                "</if>" +
            "</script>")
    Page<Setmeal> findPage(@Param("condition") String queryString);

    @Select("select * from t_setmeal where id = #{mid}")
    @Results({
            @Result(
                    property = "checkGroups",
                    column = "id",
                    javaType = List.class,
                    many = @Many(select = "cn.itcast.mapper.CheckGroupMapper.findSetMealCheckGroups")
            )
    })
    Setmeal findById(String mid);

    //修改套餐表信息
    @Update("update t_setmeal set name = #{name}, code = #{code}, helpCode = #{helpCode}," +
            " sex = #{sex}, age = #{age}, price = #{price}, remark = #{remark}, attention = #{attention}, img = #{img} where id = #{id}")
    void updateSetMeal(Setmeal setmeal);

    //删除套餐和检查项关联表中的关联关系
    @Delete("delete from t_setmeal_checkgroup where setmeal_id = #{id}")
    void clearCheckGroups(Integer id);

    //根据id删除套餐
    @Delete("delete from t_setmeal where id = #{id}")
    void deleteById(Integer id);

    //查询所有套餐信息
    @Select("select * from t_setmeal")
    List<Setmeal> findAllSetMeal();
}
