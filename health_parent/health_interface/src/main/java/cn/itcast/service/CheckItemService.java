package cn.itcast.service;

import cn.itcast.domain.CheckItem;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;

import java.util.List;

public interface CheckItemService {

    //新增检查项
    void add(CheckItem checkItem);

    //根据条件查询并分页展示
    PageResult findPage(QueryPageBean queryPageBean);

    //删除数据信息
    void delete(String id);

    //根据id查询数据
    CheckItem findById(String id);

    //修改检查项信息
    void update(CheckItem checkItem);

    //查询所有检查项
    List<CheckItem> findAll();

}
