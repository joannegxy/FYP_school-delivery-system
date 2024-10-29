package com.fyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.fyp.pojo.Order;

public interface OrderService extends IService<Order> {
//    PageInfo<Order> listOrder(Integer id);

    //根据订单id获取此订单(包含product和customer的信息)
    Order getOrderById(Integer id);

    PageInfo<Order> listOrder(Integer sid);

    PageInfo<Order> listToBeDeliveredOrder(Integer sid);

    PageInfo<Order> listMyOrders(Integer userId,String itemName);

    Order getInvoiceById(Integer orderId);

    PageInfo<Order> filterOrderbyStatus(Integer sid,Integer statusid);

    PageInfo <Order> filtermyOrderbyStatus(Integer userId, Integer statusid);

    PageInfo <Order> listDeliveringOrder(Integer sid);

    PageInfo <Order> checkOrderbyDeliveryman(Integer did);
}
