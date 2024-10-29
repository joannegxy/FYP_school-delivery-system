package com.fyp.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyp.pojo.Item;
import com.fyp.pojo.Order;
import com.fyp.service.DeliveringOrderService;
import com.fyp.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.fyp.pojo.ShopOwner;
import com.fyp.pojo.Store;
import com.fyp.service.ShopOwnerService;
import com.fyp.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("deliveringorder")
public class DeliveringOrderController {

    @Autowired
    private DeliveringOrderService deliveringorderService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/listDeliveringOrder")
    public String list(Model model, HttpServletRequest request){

        Object did=request.getSession().getAttribute("deliverymanId");
        PageInfo<Order> list = deliveringorderService.listDeliveringOrder((Integer)did);
        model.addAttribute("pageInfo",list);
        return "deliveryman-order-list";

    }

    @RequestMapping("/alreadyDelivered/{orderId}")
    public String alreadyDelivered(@PathVariable Integer orderId){
        Order order = orderService.getById(orderId);
        order.setStatus(5);
        orderService.updateById(order);
        return "redirect:/deliveringorder/listDeliveringOrder";
    }

    @RequestMapping("orderstatusfilter/{statusid}")
    public String orderStatusFilter(@PathVariable Integer statusid,HttpServletRequest request, Model model){

        Object sid=request.getSession().getAttribute("shopId");
        PageInfo<Order> list = deliveringorderService.filterOrderbyStatus((Integer)sid,statusid);
        model.addAttribute("pageInfo",list);
        return "deliveryman-order-list";
    }

}
