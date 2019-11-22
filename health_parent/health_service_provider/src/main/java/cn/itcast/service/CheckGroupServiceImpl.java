package cn.itcast.service;

import cn.itcast.domain.CheckGroup;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.mapper.CheckGroupMapper;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupMapper checkGroupMapper;

    //新增检查项
    @Override
    public void addCheckGroup(CheckGroup checkGroup, String[] ids) {
        checkGroupMapper.addCheckGroup(checkGroup);
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                checkGroupMapper.addCheckGroupAndItem(checkGroup.getId(), id);
            }
        }
    }

    //根据查询条件进行查询并进行分页
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckGroup> page = checkGroupMapper.findCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //根据id进行数据删除
    @Override
    public void deleteById(String id) {
        //进行检查组的删除之前需要先清空检查组--检查项表中的关联关系，所以需要先根据id清空关联表中的数据
        checkGroupMapper.clearByGid(id);
        //然后进行检查组数据的删除
        checkGroupMapper.deleteById(id);
    }

    //根据id查询数据
    @Override
    public CheckGroup findById(String id) {
        return checkGroupMapper.findById(id);
    }

    //根据表单数据对检查项进行修改
    @Override
    public void update(CheckGroup checkGroup, String[] ids) {
        //首先清空关联表中group和item的关联信息
        checkGroupMapper.clearByGid(checkGroup.getId().toString());
        //然后重新添加检查组和检查项的关联
        if (ids != null && ids.length > 0) {
            for (String id : ids) {
                checkGroupMapper.addCheckGroupAndItem(checkGroup.getId(), id);
            }
        }
        //进行检查组基本信息的修改
        checkGroupMapper.updateCheckGroup(checkGroup);
    }

    //查询所有检查项
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupMapper.findAll();
    }
}
