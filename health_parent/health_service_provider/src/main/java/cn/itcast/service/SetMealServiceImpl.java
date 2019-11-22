package cn.itcast.service;

import cn.itcast.constant.RedisConstant;
import cn.itcast.domain.Setmeal;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.mapper.SetMealMapper;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.List;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private JedisPool jedisPool;

    //添加套餐信息
    @Override
    public void add(Setmeal setmeal, String[] checkgroupIds) {
        //添加套餐需要对两张表进行操作，套餐表和关系表，首先操作套餐表
        setMealMapper.addMeal(setmeal);
        //然后对关系表进行操作, 这里需要对数组进行一个判断，不为null且长度大于0才可以
        if (checkgroupIds!=null && checkgroupIds.length > 0) {
            for (String gid : checkgroupIds) {
                setMealMapper.addMealCheckGroup(setmeal.getId(), gid);
            }
        }
        //套餐保存完毕之后需要把图片的文件名保存到redis中
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage, pageSize);
        Page<Setmeal> page =  setMealMapper.findPage(queryString);
        return new PageResult(page.getTotal(), page.getResult());
    }

    //根据id查询套餐信息
    @Override
    public Setmeal findById(String mid) {
        return setMealMapper.findById(mid);
    }

    //修改套餐数据
    @Override
    public void update(Setmeal setmeal, String[] ids) {
        //修改套餐表中的基本数据信息
        setMealMapper.updateSetMeal(setmeal);
        //清空套餐与检查项之间的关联关系
        setMealMapper.clearCheckGroups(setmeal.getId());
        //新增套餐与检查项之间的关联关系
        if (ids!=null && ids.length > 0) {
            for (String id : ids) {
                setMealMapper.addMealCheckGroup(setmeal.getId(), id);
            }
        }
        //修改之后把当前使用的图片路径添加到redis中
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
    }

    @Override
    public void delete(Integer id) {
        //删除掉套餐信息之后该套餐对应的图片称为垃圾图片，需要从redis中删除
        Setmeal setmeal = setMealMapper.findById(id.toString());
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getId().toString());
        //删除之前需要清除套餐和检查项之间的关联关系
        setMealMapper.clearCheckGroups(id);
        //在套餐表中删除该套餐的信息
        setMealMapper.deleteById(id);

    }

    @Override
    public List<Setmeal> findAllSetMeal() {
        return setMealMapper.findAllSetMeal();
    }
}
