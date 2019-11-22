package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.domain.CheckGroup;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    //接受检查组信息及检查组关联的检查项信息并进行存储
    @PreAuthorize("hasAuthority('CHECKGROUP_ADD')")
    @RequestMapping("/add")
    public Result add(@RequestBody CheckGroup checkGroup, String[] ids) {
        try {
            checkGroupService.addCheckGroup(checkGroup, ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }

    //接受条件并进行分页查询
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        return checkGroupService.findPage(queryPageBean);
    }

    //根据id删除数据
    @RequestMapping("/delete")
    public Result delete(String id) {
        try {
            checkGroupService.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    //根据id进行查询数据
    @RequestMapping("/findById")
    public Result findById(String id) {
        CheckGroup checkGroup = null;
        try {
            checkGroup = checkGroupService.findById(id);
            System.out.println(checkGroup.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "");
        }
        return new Result(true, "", checkGroup);
    }

    //修改检查项中的数据
    @RequestMapping("/update")
    public Result update(@RequestBody CheckGroup checkGroup, String[] ids) {
        try {
            checkGroupService.update(checkGroup, ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    //查询所有的检查项
    @RequestMapping("/findAll")
    public Result findAll() {
        List<CheckGroup> list = null;
        try {
            list = checkGroupService.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "");
        }
        return new Result(true, "", list);
    }
}
