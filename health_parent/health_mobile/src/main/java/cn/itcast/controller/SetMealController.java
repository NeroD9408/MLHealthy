package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.domain.Setmeal;
import cn.itcast.entity.Result;
import cn.itcast.service.SetMealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    //查询所有套餐信息
    @RequestMapping("/findAllSetMeal")
    public Result findAllSetMeal() {
        try {
            List<Setmeal> list = setMealService.findAllSetMeal();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }

    //根据id查询套餐详情
    @RequestMapping("/findById")
    public Result findById(String id) {
        try {
            Setmeal setmeal = setMealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
