package com.fyp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fyp.pojo.Order;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper extends BaseMapper<Order> {
    List<Order> listOrder(Integer sid);

    List<Order> listToBeDeliveredOrder(Integer sid);

    Order getOrderById(Integer id);

    List<Order> listMyOrders(Integer userId,String itemName);

    Order invoice(Integer orderId);

    List<Order> filterOrderbyStatus(Integer sid, Integer statusid);

    List<Order> filtermyOrderbyStatus(Integer userId, Integer statusid);

    List<Order> listDeliveringOrder(Integer sid);
    List<Order> checkOrderbyDeliveryman(Integer did);
}
