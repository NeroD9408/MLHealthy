package cn.itcast.mapper;

import cn.itcast.domain.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface CheckItemMapper {

    //新增检查项
    @Insert("insert into t_checkitem values(null, #{code}, #{name}," +
            " #{sex}, #{age}, #{price}, #{type}, #{attention}, #{remark})")
    void add(CheckItem checkItem);

    //条件查询
    @Select({
            "<script>" +
                "select * from t_checkitem where 1=1 " +
                    "<if test='condition != null and condition.length &gt; 0'>" +
                        "and code like '%${condition}%' or name like '%${condition}%'" +
                    "</if>" +
            "</script>"
    })
    Page<CheckItem> findCondition(@Param("condition") String queryString);

    //根据id删除
    @Delete("delete from t_checkitem where id = #{id}")
    void delete(String id);

    //根据id进行查询
    @Select("select * from t_checkitem where id = #{id}")
    CheckItem findById(String id);

    //根据对象中的数据进行数据修改
    @Update("update t_checkitem set code = #{code}, name = #{name}, sex = #{sex}," +
            " age = #{age}, price = #{price}, type = #{type}," +
            " attention = #{attention}, remark = #{remark} where id = #{id}")
    void update(CheckItem checkItem);

    //查询所有检查项
    @Select("select * from t_checkitem")
    List<CheckItem> findAll();

    //根据groupId查询itemId
    @Select("select * from t_checkitem where id in(select checkitem_id from t_checkgroup_checkitem where checkgroup_id = #{gid})")
    List<CheckItem> findByGid(String gid);

    //根据item的id清除关系表中的关系
    @Delete("delete from t_checkgroup_checkitem where checkitem_id = #{id}")
    void clearById(String id);
}
