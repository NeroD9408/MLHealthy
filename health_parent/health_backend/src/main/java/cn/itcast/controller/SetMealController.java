package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisConstant;
import cn.itcast.domain.Setmeal;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.service.SetMealService;
import cn.itcast.utils.QiniuUtils;
import cn.itcast.utils.UuidUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @Autowired
    private JedisPool jedisPool;

    //上传图片
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile imgFile) {
        //接收到上传的文件，上传到七牛云
        String originalFilename = imgFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String sufix = originalFilename.substring(index - 1);
        String fileName = UuidUtil.getUuid() + sufix;
        try {
            QiniuUtils.upload2Qiniu(imgFile.getBytes(), fileName);
            //上传之后把文件名保存到redis中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES, fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, fileName);
    }

    //新增套餐信息
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, String[] checkgroupIds) {
        //获取到前台传递的参数，调用service进行储存
        try {
            setMealService.add(setmeal, checkgroupIds);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    //根据搜索条件查询并分页返回数据
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            return setMealService.findPage(queryPageBean);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //根据id进行查询，数据回显
    @RequestMapping("/findById")
    public Result findById(String mid) {
        Setmeal setmeal = null;
        try {
            setmeal = setMealService.findById(mid);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "");
        }
        return new Result(true, "", setmeal);
    }

    //修改套餐信息
    @RequestMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, String[] ids) {
        try {
            setMealService.update(setmeal, ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    //删除套餐信息
    @RequestMapping("/delete")
    public Result delete(Integer id) {
        try {
            setMealService.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }
}
