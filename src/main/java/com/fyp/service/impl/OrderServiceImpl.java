package com.fyp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.fyp.mapper.OrderMapper;
import com.fyp.pojo.Order;
import com.fyp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
//    @Override
//    public PageInfo<Order> listOrder(Integer id) {
//        List<Order>list=orderMapper.listOrder(id);
//        return new PageInfo<>(list);
//    }

    @Override
    public Order getOrderById(Integer id) {

        return orderMapper.getOrderById(id) ;
    }

    @Override
    public PageInfo<Order> listOrder(Integer sid) {
        List<Order>list=orderMapper.listOrder(sid);
        return new PageInfo<>(list);
    }

    public PageInfo<Order> listToBeDeliveredOrder(Integer sid) {
        List<Order>list=orderMapper.listToBeDeliveredOrder(sid);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<Order> listMyOrders(Integer userId,String itemName) {
        List<Order>list=orderMapper.listMyOrders(userId,itemName);
        return new PageInfo<>(list);
    }

    @Override
    public PageInfo<Order> filterOrderbyStatus(Integer sid,Integer statusid){
        List<Order>list=orderMapper.filterOrderbyStatus(sid,statusid);
        return  new PageInfo<>(list);
    }
    @Override
    public PageInfo <Order> filtermyOrderbyStatus(Integer userId, Integer statusid){
        List<Order>list=orderMapper.filtermyOrderbyStatus(userId,statusid);
        return  new PageInfo<>(list);
    }
    @Override
    public PageInfo <Order> listDeliveringOrder(Integer sid){
        List<Order>list=orderMapper.listDeliveringOrder(sid);
        return new PageInfo<>(list);
    }
    @Override
    public PageInfo <Order> checkOrderbyDeliveryman(Integer did){
        List<Order>list=orderMapper.checkOrderbyDeliveryman(did);
        return new PageInfo<>(list);
    }


    @Override
    public Order getInvoiceById(Integer orderId) {
        return orderMapper.invoice(orderId);
    }
}
