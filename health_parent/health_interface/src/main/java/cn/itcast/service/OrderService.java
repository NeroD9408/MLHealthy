package cn.itcast.service;

import cn.itcast.entity.Result;

import java.util.Map;

public interface OrderService {

    //进行预约操作
    Result order(Map map) throws Exception;

    //根据id查询预约信息
    Map<String, Object> getOrderDetail(Integer id) throws Exception;
}
