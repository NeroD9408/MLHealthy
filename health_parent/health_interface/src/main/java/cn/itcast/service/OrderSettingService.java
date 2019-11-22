package cn.itcast.service;

import cn.itcast.domain.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    //将excel解析出来的数据添加到数据库
    void add(List<OrderSetting> data);

    //获取对应月份的预约信息
    List<Map> findByMonth(String date);

    //根据日期修改对应的预约人数
    void editNumberByDate(Date date, Integer number);
}
