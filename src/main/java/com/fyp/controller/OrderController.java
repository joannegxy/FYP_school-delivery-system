package com.fyp.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyp.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.fyp.pojo.*;
import com.fyp.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ItemService itemService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private DeliverymanService deliverymanService;
    @Autowired
    private DeliveringOrderService deliveringorderService;

    //查询所有订单
    @RequestMapping("/listOrder")
    public String list( Model model, HttpServletRequest request){

        Object sid=request.getSession().getAttribute("shopId");
        PageInfo<Order> list = orderService.listOrder((Integer)sid);
        model.addAttribute("pageInfo",list);
        return "order-list";
    }

    //跳转到添加订单页面
    @RequestMapping("preSaveOrder")
    public String preBorrow(Model model) {
        List<Item> itemList = itemService.list(null);
        List<Customer> customerList = customerService.list(null);
        model.addAttribute("customerList",customerList);
        model.addAttribute("itemList",itemList);
        return "order-save";
    }

    //添加订单
    @RequestMapping("saveOrder")
    public String borrow(Integer cid,Integer iid,Integer count,Model model){
        Order order = new Order();
        order.setCid(cid);
        order.setIid(iid);
        Item byId = itemService.getById(iid);
        if (count>byId.getStock()){
            model.addAttribute("msg","此商品库存不足，无法购买");
            return "order-save";
        }
        Double total=byId.getPrice()*count;
        order.setTotal(total);
        order.setCount(count);
        boolean save = orderService.save(order);
        //库存减去订单数量
        byId.setStock(byId.getStock()-count);
        boolean b = itemService.updateById(byId);
        return "redirect:/order/listOrder";
    }

    @RequestMapping("orderstatusfilter/{statusid}")
    public String orderStatusFilter(@PathVariable Integer statusid,HttpServletRequest request, Model model){

        Object sid=request.getSession().getAttribute("shopId");
        PageInfo<Order> list = orderService.filterOrderbyStatus((Integer)sid,statusid);
        model.addAttribute("pageInfo",list);
        return "order-list";
    }

    @RequestMapping("listDeliveringOrder")
    public String listDeliveringOrder(HttpServletRequest request, Model model){
        Object sid=request.getSession().getAttribute("shopId");

        QueryWrapper<Deliveryman> qw=new QueryWrapper<>();
        qw.eq("shop_id",sid);
        List<Deliveryman> deliverymanList = deliverymanService.list(qw);
        model.addAttribute("deliverymanList",deliverymanList);

        PageInfo<Order> list = orderService.listDeliveringOrder((Integer)sid);
        model.addAttribute("pageInfo",list);
        return "checkdeliverymanorder";
    }

    @RequestMapping("checkDeliverymanOrder/{did}")
    public String checkDeliverymanOrder(@PathVariable Integer did,HttpServletRequest request, Model model){

        Object sid=request.getSession().getAttribute("shopId");
        QueryWrapper<Deliveryman> qw=new QueryWrapper<>();
        qw.eq("shop_id",sid);
        List<Deliveryman> deliverymanList = deliverymanService.list(qw);
        model.addAttribute("deliverymanList",deliverymanList);

        PageInfo<Order> list = orderService.checkOrderbyDeliveryman(did);
        model.addAttribute("pageInfo",list);
        return "checkdeliverymanorder";
    }

    //删除订单
    @RequestMapping("delOrder/{id}")
    public String delReaderBook(@PathVariable("id") Integer id){
        boolean b = orderService.removeById(id);
        return "redirect:/order/listOrder";
    }

    //批量删除
    @PostMapping("batchDeleteOrder")
    @ResponseBody
    public String batchDeleteReaderBook(String idList){
        String[]split= StrUtil.split(idList,",");
        List<Integer> list=new ArrayList<>();

        for (String s : split) {
            if (!s.isEmpty()){
                int i = Integer.parseInt(s);
                list.add(i);
            }
        }
        boolean b = orderService.removeByIds(list);
        if (b){
            return "OK";
        }else {
            return "error";
        }
    }

    //批量删除
    @PostMapping("batchDelivery")
    @ResponseBody
    public String batchDeliveryReaderBook(String idList, Integer deliveryman) throws Exception {

        String[]split= StrUtil.split(idList,",");
        List<Integer> list=new ArrayList<>();

        for (String s : split) {
            if (!s.isEmpty()){
                int i = Integer.parseInt(s);
                list.add(i);
            }
        }

        List<DeliveringOrder> deliveringorderlist=new ArrayList<>();
        for(Integer orderid:list){

            int save=deliveringorderService.addDeliveringOrder(orderid,deliveryman);
            Order order=orderService.getOrderById(orderid);
            Customer customer=customerService.getById(order.getCid());
            String email=customer.getEmail();
            Item item=itemService.getById(order.getIid());
            String itemname=item.getItemName();
            MailUtils.sendMail(email,"您订购的"+itemname+"正在配送中，请注意查收","餐馆邮件");
            order.setStatus(4);
            orderService.updateById(order);

        }

        boolean b = deliveringorderService.saveBatch(deliveringorderlist);
        if (b){
            return "OK";
        }else {
            return "error";
        }
    }

    @RequestMapping("packingOrder/{orderId}")
    public String packingOrder(@PathVariable Integer orderId, HttpSession session,Model model){
        Order order = orderService.getById(orderId);
        if(order.getStatus()==1) {
            session.setAttribute("msg","已经在打包中，请选择其他订单来打包");
            return "redirect:/order/listOrder";
        }
        order.setStatus(1);
        orderService.updateById(order);
        session.removeAttribute("msg");
        return "redirect:/order/listOrder";
    }

    @RequestMapping("donepackingOrder/{orderId}")
    public String donepackingOrder(@PathVariable Integer orderId,HttpSession session){
        Order order = orderService.getById(orderId);
        order.setStatus(2);
        orderService.updateById(order);
        session.removeAttribute("msg");//用来移除packingorder以防被两个工作人员同时打包的信息
        return "redirect:/order/listOrder";
    }

    @RequestMapping("prepareforDelivery/{orderId}")
    public String prepareforDelivery(@PathVariable Integer orderId,HttpSession session){
        Order order = orderService.getById(orderId);
        order.setStatus(3);
        orderService.updateById(order);
        session.removeAttribute("msg");//用来移除packingorder以防被两个工作人员同时打包的信息
        return "redirect:/order/listOrder";
    }

    @RequestMapping("cancelprepareforDelivery/{orderId}")
    public String cancelprepareforDelivery(@PathVariable Integer orderId, HttpSession session){
        Order order = orderService.getById(orderId);
        order.setStatus(2);
        orderService.updateById(order);
        session.removeAttribute("msg");//用来移除packingorder以防被两个工作人员同时打包的信息
        return "redirect:/order/listOrder";
    }

    @RequestMapping("cancelDelivery/{orderId}")
    public String cancelDelivery(@PathVariable Integer orderId, HttpSession session){
        Order order = orderService.getById(orderId);
        order.setStatus(2);
        orderService.updateById(order);
        deliveringorderService.removeById(orderId);
        session.removeAttribute("msg");//用来移除packingorder以防被两个工作人员同时打包的信息
        return "redirect:/order/listOrder";
    }

    @RequestMapping("unarrangedDelivery")
    public String unarrangedDelivery(HttpServletRequest request, Model model){
        Object sid=request.getSession().getAttribute("shopId");
        PageInfo<Order> list = orderService.listToBeDeliveredOrder((Integer)sid);
        model.addAttribute("pageInfo",list);
        QueryWrapper<Deliveryman> qw=new QueryWrapper<>();
        qw.eq("shop_id",sid);
        List<Deliveryman> deliverymanList = deliverymanService.list(qw);
        model.addAttribute("deliverymanList",deliverymanList);
        return "unarrangeddelivery";
    }

    @RequestMapping("sendMessage/{email}/{itemName}/{orderId}")
    public String sendMessage(@PathVariable String email,@PathVariable String itemName,@PathVariable Integer orderId){
        MailUtils.sendMail(email,"您订购的"+itemName+"正在配送中，请注意查收","餐馆邮件");
        Order byId = orderService.getById(orderId);
        byId.setStatus(4);
        orderService.updateById(byId);
        return "redirect:/order/listOrder";
    }
}
