package cn.itcast.service;

import cn.itcast.domain.CheckGroup;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import com.github.pagehelper.Page;

import java.util.List;

public interface CheckGroupService {

    //新增检查项
    void addCheckGroup(CheckGroup checkGroup, String[] ids);

    //根据查询条件进行查询并进行分页返回数据
    PageResult findPage(QueryPageBean queryPageBean);

    //根据id进行数据的删除
    void deleteById(String id);

    //根据id查询
    CheckGroup findById(String id);

    //根据表单数据修改检查项
    void update(CheckGroup checkGroup, String[] ids);

    //查询所有检查项
    List<CheckGroup> findAll();

}
