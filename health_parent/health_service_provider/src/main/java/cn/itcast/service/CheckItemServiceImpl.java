package cn.itcast.service;

import cn.itcast.domain.CheckItem;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.mapper.CheckItemMapper;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemMapper checkItemMapper;

    //新增检查项
    @Override
    public void add(CheckItem checkItem) {
        checkItemMapper.add(checkItem);
    }

    //根据条件查询并返回分页对象
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<CheckItem> page = checkItemMapper.findCondition(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //根据id删除数据
    @Override
    public void delete(String id) {
        //删除之前需要将检查组检查项的中间表进行关系的清除
        checkItemMapper.clearById(id);
        //删除checkitem表中的数据
        checkItemMapper.delete(id);
    }

    //根据id查询数据
    @Override
    public CheckItem findById(String id) {
        return checkItemMapper.findById(id);
    }

    //修改检查项信息
    @Override
    public void update(CheckItem checkItem) {
        checkItemMapper.update(checkItem);
    }

    //查询所有检查项
    @Override
    public List<CheckItem> findAll() {
        return checkItemMapper.findAll();
    }
}
