package com.fyp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fyp.mapper.DeliveringOrderMapper;
import com.fyp.pojo.DeliveringOrder;
import com.fyp.service.DeliveringOrderService;
import com.github.pagehelper.PageInfo;
import com.fyp.mapper.OrderMapper;
import com.fyp.pojo.Order;
import com.fyp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveringOrderServiceImpl extends ServiceImpl<DeliveringOrderMapper, DeliveringOrder>implements DeliveringOrderService {

    @Autowired
    private DeliveringOrderMapper deliveringOrderMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public PageInfo<Order> listDeliveringOrder(Integer did) {
        List<Order> list = deliveringOrderMapper.listDeliveringOrder(did);
        return new PageInfo<>(list);
    }
    @Override
    public int addDeliveringOrder(Integer id, Integer did){
        return deliveringOrderMapper.addDeliveringOrder(id,did);
    }

    @Override
    public PageInfo<Order> filterOrderbyStatus(Integer sid,Integer statusid){
        List<Order>list=orderMapper.filterOrderbyStatus(sid,statusid);
        return  new PageInfo<>(list);
    }

}
