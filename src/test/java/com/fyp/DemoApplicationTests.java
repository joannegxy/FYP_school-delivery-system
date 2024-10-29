package com.fyp;

import com.fyp.mapper.OrderMapper;
import com.fyp.pojo.Order;
import com.fyp.service.ItemService;
import com.fyp.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private ItemService itemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderMapper orderMapper;


    @Test
    void test1() {
        Order invoice = orderService.getInvoiceById(1);
        System.out.println(invoice.toString());

    }

}
