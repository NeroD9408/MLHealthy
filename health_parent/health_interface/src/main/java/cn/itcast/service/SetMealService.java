package cn.itcast.service;

import cn.itcast.domain.Setmeal;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;

import java.util.List;

public interface SetMealService {

    //添加套餐信息
    void add(Setmeal setmeal, String[] checkgroupIds);

    //条件查询并分页展示
    PageResult findPage(QueryPageBean queryPageBean);

    //根据id查询套餐信息
    Setmeal findById(String mid);

    //修改套餐中的数据信息
    void update(Setmeal setmeal, String[] ids);

    //删除套餐
    void delete(Integer id);

    //查询所有套餐信息
    List<Setmeal> findAllSetMeal();

}
