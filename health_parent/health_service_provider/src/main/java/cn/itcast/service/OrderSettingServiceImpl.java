package cn.itcast.service;

import cn.itcast.domain.OrderSetting;
import cn.itcast.mapper.OrderSettingMapper;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingMapper orderSettingMapper;

    //将excel解析出来的数据添加到数据库
    @Override
    public void add(List<OrderSetting> data) {
        if (data != null && data.size() > 0) {
            for (OrderSetting orderSetting : data) {
                //为了避免同一天重复添加预约人数，所以需要对同一天的数据进行控制
                //查询预约当天有没有设置预约人数，如果已经设置，那么进行修改操作，如何没有设置，那么进行添加操作
                if (orderSettingMapper.findCountByOrderDate(orderSetting.getOrderDate()) > 0) {
                    //说明预约的日期已经设置的预约人数
                    orderSettingMapper.editNumberByOrderDate(orderSetting);
                } else {
                    //说明预约的日期并没有设置预约的人数
                    orderSettingMapper.add(orderSetting);
                }
            }
        }
    }

    //获取对应月份的预约信息
    @Override
    public List<Map> findByMonth(String date) {
        List<Map> list = new ArrayList<>();
        //拼接开始的查询日期
        String begin = date + "-1";//2019-11-1
        //拼接结束的查询日期，因为每月最多只有31天，所以可以把结束日期直接定义成31，这样就不需要判断每月有多少天
        String end = date + "-31";//2019-11-31
        List<OrderSetting> orderSettings = orderSettingMapper.findByMonth(begin, end);
        if (orderSettings != null && orderSettings.size() > 0) {
            for (OrderSetting orderSetting : orderSettings) {
                Map<String, Object> map = new HashMap<>();
                map.put("date", orderSetting.getOrderDate().getDate());
                map.put("number", orderSetting.getNumber());
                map.put("reservations", orderSetting.getReservations());
                list.add(map);
            }
        }
        return list;
    }

    //根据对应日期修改预约人数
    @Override
    public void editNumberByDate(Date date, Integer number) {
        //为了可以重复使用mapper中的方法，首先对date进行解析生成字符串类型的日期
        String str_date = new SimpleDateFormat("yyyy/MM/dd").format(date);
        //创建OrderSetting对象并赋值
        OrderSetting orderSetting = new OrderSetting(new Date(str_date), number);
        //查询传递的日期是否已经设置预约人数
        long count = orderSettingMapper.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0) {
            //说明已经设置了预约人数, 那么需要做修改的操作
            //修改日期对应的预约人数
            orderSettingMapper.editNumberByOrderDate(orderSetting);
        } else {
            //说明没有设置预约人数，那么需要做添加的操作
            orderSettingMapper.add(orderSetting);
        }
    }
}
