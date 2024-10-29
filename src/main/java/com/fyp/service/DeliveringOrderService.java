package com.fyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fyp.pojo.DeliveringOrder;
import com.github.pagehelper.PageInfo;
import com.fyp.pojo.Order;

public interface DeliveringOrderService extends IService<DeliveringOrder> {

    PageInfo<Order> listDeliveringOrder(Integer did);

    int addDeliveringOrder(Integer id, Integer did);

    PageInfo<Order> filterOrderbyStatus(Integer sid,Integer statusid);

}
