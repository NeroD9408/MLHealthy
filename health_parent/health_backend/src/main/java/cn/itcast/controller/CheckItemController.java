package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.domain.CheckItem;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    //添加检查项
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    //条件查询并进行分页展示
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return checkItemService.findPage(queryPageBean);
    }

    //删除数据信息
    @RequestMapping("/delete")
    public Result delete(String id) {
        try {
            checkItemService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    //根据id查询信息，进行数据回显
    @RequestMapping("/findById")
    public Result findById(String id) {
        CheckItem checkItem = null;
        try {
            checkItem = checkItemService.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "");
        }
        return new Result(true, "", checkItem);
    }

    //进行数据的修改
    @RequestMapping("/update")
    public Result update(@RequestBody CheckItem checkItem) {
        try {
            checkItemService.update(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            //修改过程出现异常，修改失败
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    //查询所有检查项的信息
    @RequestMapping("/findAll")
    public Result findAll() {
        List<CheckItem> list = null;
        try {
            list = checkItemService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true, "", list);
    }
}
