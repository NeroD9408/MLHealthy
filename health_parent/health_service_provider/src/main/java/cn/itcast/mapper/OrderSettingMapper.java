package cn.itcast.mapper;


import cn.itcast.domain.OrderSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

public interface OrderSettingMapper {

    //添加
    @Insert("insert into t_ordersetting(orderDate, number, reservations) values(#{orderDate}, #{number}, #{reservations})")
    void add(OrderSetting orderSetting);

    //根据预约日期修改预约人数
    @Update("update t_ordersetting set number = #{number} where orderDate = #{orderDate}")
    void editNumberByOrderDate(OrderSetting orderSetting);

    //查询预约日期有没有设置预约天数
    @Select("select count(*) from t_ordersetting where orderDate = #{orderDate}")
    long findCountByOrderDate(Date orderDate);

    //根据起始和结束日期来查询月份的预约信息
    @Select("select * from t_ordersetting where orderDate between #{begin} and #{end}")
    List<OrderSetting> findByMonth(@Param("begin") String begin, @Param("end") String end);

    //根据日期来查询预约对象
    @Select("select * from t_ordersetting where orderDate = #{date}")
    OrderSetting findByOrderDate(Date date);

    //根据日期来修改已经预约人数
    @Update("update t_ordersetting set reservations = #{reservations} where orderDate = #{orderDate}")
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
