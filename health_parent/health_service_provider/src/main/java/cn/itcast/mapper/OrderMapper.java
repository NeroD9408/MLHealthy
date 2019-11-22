package cn.itcast.mapper;

import cn.itcast.domain.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface OrderMapper {

    //根据条件查询Order信息
    @Select("<script>" +
            "select * from t_order" +
                "<where>" +
                    "<if test='id != null'>" +
                        " and id = #{id}" +
                    "</if>" +
                    "<if test='memberId != null'>" +
                        " and member_id = #{memberId}" +
                    "</if>" +
                    "<if test='orderDate != null'>" +
                        " and orderDate = #{orderDate}" +
                    "</if>" +
                    "<if test='orderType != null'>" +
                        " and orderType = #{orderType}" +
                    "</if>" +
                    "<if test='orderStatus != null'>" +
                        " and orderStatus = #{orderStatus}" +
                    "</if>" +
                    "<if test='setmealId != null'>" +
                        " and setmeal_id = #{setmealId}" +
                    "</if>" +
                "</where>" +
            "</script>")
    List<Order> findByCondition(Order order);

    //添加预约信息到数据库
    @Insert("insert into t_order values(null,#{memberId},#{orderDate},#{orderType},#{orderStatus},#{setmealId})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    void add(Order order);

    //根据id查询预约信息
    @Select("select * from t_order where id = #{id}")
    Order findById(Integer id);
}
