package com.fyp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fyp.pojo.DeliveringOrder;
import com.fyp.pojo.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveringOrderMapper extends BaseMapper<DeliveringOrder> {

    List<Order> listDeliveringOrder(Integer did);

    int addDeliveringOrder(Integer id, Integer did);

    List<Order> filterOrderbyStatus(Integer sid, Integer statusid);

}
