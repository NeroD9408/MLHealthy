package cn.itcast.controller;


import cn.itcast.constant.MessageConstant;
import cn.itcast.domain.OrderSetting;
import cn.itcast.entity.Result;
import cn.itcast.service.OrderSettingService;
import cn.itcast.utils.POIUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;

    //上传excel表格文件，批量导入数据到数据库
    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        try {
            //获取上传的excel文件
            List<String[]> list = POIUtils.readExcel(excelFile);
            List<OrderSetting> data = new ArrayList<>();
            if (list != null && list.size() > 0) {
                for (String[] strings : list) {
                    //获取第一个单元格中的内容
                    String orderDate = strings[0];
                    //获取第二个单元格中的内容
                    String number = strings[1];
                    OrderSetting orderSetting = new OrderSetting(new Date(orderDate), Integer.parseInt(number));
                    data.add(orderSetting);
                }
                //调用service实现批量导入
                orderSettingService.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
    }

    //查询月份的预约信息
    @RequestMapping("/findByMonth")
    public Result findByMonth(String date) {
        List<Map> list = null;
        try {
            list = orderSettingService.findByMonth(date);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
    }

    //根据日期修改对应的预约人数
    @RequestMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody Date date, Integer number) {
        try {
            orderSettingService.editNumberByDate(date, number);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ORDERSETTING_FAIL);
        }
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }
}
