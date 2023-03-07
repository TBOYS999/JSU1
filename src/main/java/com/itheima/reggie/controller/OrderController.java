package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.OrdersDto;
import com.itheima.reggie.entity.OrderDetail;
import com.itheima.reggie.entity.Orders;
import com.itheima.reggie.service.OrderDetailService;
import com.itheima.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }
    //抽离的一个方法，通过订单id查询订单明细，得到一个订单明细的集合
    //这里抽离出来是为了避免在stream中遍历的时候直接使用构造条件来查询导致eq叠加，从而导致后面查询的数据都是null
    public List<OrderDetail> getOrderDetailListByOrderId(Long orderId){
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);
        return orderDetailList;
    }

    /**
     * 用户端展示自己的订单分页查询
     * @param page
     * @param pageSize
     * @return
     * 遇到的坑：原来分页对象中的records集合存储的对象是分页泛型中的对象，里面有分页泛型对象的数据
     * 开始的时候我以为前端只传过来了分页数据，其他所有的数据都要从本地线程存储的用户id开始查询，
     * 结果就出现了一个用户id查询到 n个订单对象，然后又使用 n个订单对象又去查询 m 个订单明细对象，
     * 结果就出现了评论区老哥出现的bug(嵌套显示数据....)
     * 正确方法:直接从分页对象中获取订单id就行，问题大大简化了......
     */
    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize){
        //分页构造器对象
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> pageDto = new Page<>(page,pageSize);
        //构造条件查询对象
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        //这里是直接把当前用户分页的全部结果查询出来，要添加用户id作为查询条件，否则会出现用户可以查询到其他用户的订单情况
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo,queryWrapper);

        //通过OrderId查询对应的OrderDetail
        LambdaQueryWrapper<OrderDetail> queryWrapper2 = new LambdaQueryWrapper<>();

        //对OrderDto进行需要的属性赋值
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> orderDtoList = records.stream().map((item) ->{
            OrdersDto orderDto = new OrdersDto();
            //此时的orderDto对象里面orderDetails属性还是空 下面准备为它赋值
            Long orderId = item.getId();//获取订单id
            List<OrderDetail> orderDetailList = this.getOrderDetailListByOrderId(orderId);
            BeanUtils.copyProperties(item,orderDto);
            //对orderDto进行OrderDetails属性的赋值
            orderDto.setOrderDetails(orderDetailList);
            return orderDto;
        }).collect(Collectors.toList());

        //使用dto的分页有点难度.....需要重点掌握
        BeanUtils.copyProperties(pageInfo,pageDto,"records");
        pageDto.setRecords(orderDtoList);
        return R.success(pageDto);
    }
//    /**
//     * 页面显示
//     * @param page
//     * @param pageSize
//     * @return
//     */
//    @GetMapping("/page")
//    public R<Page> page(int page, int pageSize){
//        //页面构造器
//        Page<Orders> pageInfo = new Page<>(page, pageSize);
//
//        //查询所有orders表信息
//        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.orderByDesc(Orders::getOrderTime);
//        orderService.page(pageInfo, queryWrapper);
//
//        return R.success(pageInfo);
//    }
    /**
     * 页面显示——输入框查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, Long number, String beginTime, String endTime){
        //log.info("beginTime:{}",beginTime);
        //log.info("endTime:{}",endTime);
        //页面构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        //查询所有orders表信息
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //查询name
        if (number != null){
            queryWrapper.like(Orders::getNumber, number);
        }
        //查询beginTime 大于等于这个时间
        if (beginTime != null){
            queryWrapper.ge(Orders::getOrderTime, beginTime);
        }
        //查询endTime 小于等于这个时间
        if (endTime != null){
            queryWrapper.le(Orders::getOrderTime, endTime);
        }
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }
    /**
     * 订单状态修改——管理端
     * @param orders
     * @return
     */
    @PutMapping
    public R<String> order(@RequestBody Orders orders){
        //log.info("orders:{}", orders);
        Orders order = orderService.getById(orders.getId());
        if (order.getStatus() == 2){
            orders.setStatus(3);
            orderService.updateById(orders);
            return R.success("订单派送成功");
        }else {
            orders.setStatus(4);
            orderService.updateById(orders);
            return R.success("订单已完成");
        }
    }



}