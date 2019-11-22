package cn.itcast.service;

import cn.itcast.constant.MessageConstant;
import cn.itcast.domain.Member;
import cn.itcast.domain.Order;
import cn.itcast.domain.OrderSetting;
import cn.itcast.domain.Setmeal;
import cn.itcast.entity.Result;
import cn.itcast.mapper.MemberMapper;
import cn.itcast.mapper.OrderMapper;
import cn.itcast.mapper.OrderSettingMapper;
import cn.itcast.mapper.SetMealMapper;
import cn.itcast.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderSettingMapper orderSettingMapper;
    @Autowired
    private MemberMapper memberMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    //进行预约操作
    @Override
    public Result order(Map map) throws Exception {
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行 预约
        String orderDate = (String) map.get("orderDate");
        String telephone = (String) map.get("telephone");
        OrderSetting orderSetting = orderSettingMapper.findByOrderDate(DateUtils.parseString2Date(orderDate));
        if (orderSetting == null) {
            //说明预约的日期没有进行预约设置，返回错误信息
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();// 可预约人数
        int reservations = orderSetting.getReservations();// 已预约人数
        if (reservations >= number) {
            //说明已经预约满
            return new Result(false, MessageConstant.ORDER_FULL);
        }
        //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约 则无法完成再次预约
        //根据手机号来查询用户
        Member member = memberMapper.findByTelephone(telephone);
        if (member != null) {
            Integer id = member.getId();//用户id
            Date date = DateUtils.parseString2Date(orderDate);
            Integer setmealId = Integer.parseInt((String) map.get("setmealId"));
            Order order = new Order(id, date, setmealId);
            List<Order> list = orderMapper.findByCondition(order);
            if (list != null && list.size() > 0) {
                //说明用户在重复预约
                return new Result(false, MessageConstant.HAS_ORDERED);
            }
        } else {
            //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注 册并进行预约
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setName((String) map.get("name"));
            member.setSex((String) map.get("sex"));
            member.setIdCard((String) map.get("idCard"));
            member.setRegTime(new Date());
            //将member信息存入到数据库
            memberMapper.add(member);
        }
        //5、预约成功，更新当日的已预约人数
        //预约成功，将预约信息保存到数据库
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        order.setOrderType(Order.ORDERTYPE_WEIXIN);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        //添加预约信息到数据库
        orderMapper.add(order);
        orderSetting.setReservations(orderSetting.getReservations() + 1);
        orderSettingMapper.editReservationsByOrderDate(orderSetting);
        return new Result(true, MessageConstant.ORDER_SUCCESS,order.getId());
    }

    //根据id查询预约信息
    @Override
    public Map<String, Object> getOrderDetail(Integer id) throws Exception {
        //首先查询预约表中的数据
        Order order = orderMapper.findById(id);
        Integer memberId = order.getMember_id();
        Integer setmealId = order.getSetmeal_id();
        //使用会员id和套餐id分别查询数据库获取信息
        Member member = memberMapper.findById(memberId);
        Setmeal setmeal = setMealMapper.findById(setmealId.toString());
        Map<String, Object> map = new HashMap<>();
        //封装页面所需要的信息
        map.put("orderDate", DateUtils.parseDate2String(order.getOrderDate()));
        map.put("member", member.getName());
        map.put("orderType", order.getOrderType());
        map.put("setmeal", setmeal.getName());
        return map;
    }
}
