package com.fyp.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.fyp.pojo.*;
import com.fyp.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("normalUser")
@Slf4j
public class NormalUserController extends SidebarController{
    @Autowired
    private ItemService itemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private CartItemService cartitemService;
    @RequestMapping("viewstore/{storeId}")
    public String list(@RequestParam(required = false,defaultValue = "1",value = "pageNum") Integer pageNum,
                       @RequestParam(required = false,defaultValue = "10",value = "pageSize") Integer pageSize,@PathVariable Integer storeId ,Model model) {
        if (pageNum <= 0 || pageNum.equals("") || pageNum == null) {
            pageNum = 1;
        }
        if (pageSize <= 0 || pageSize.equals("") || pageSize == null) {
            pageSize = 10;
        }

        Store store=storeService.getById(storeId);
        model.addAttribute("store",store);
        QueryWrapper<Item> qw1=new QueryWrapper<>();
        qw1.eq("sid",storeId);
        qw1.notLike("status",0);

        List<Item> allitemList = itemService.list(qw1);
        model.addAttribute("allitemList",allitemList);

        PageHelper.startPage(pageNum, pageSize);

        List<Item> list2 = itemService.list(qw1);
        PageInfo<Item>pageInfo=new PageInfo<>(list2);
        model.addAttribute("pageInfo",pageInfo);
        List<Store> storeList = storeService.list(null);
        model.addAttribute("storeList",storeList);
        return "customer-item-list";
    }

    @RequestMapping("myOrders")
    public String myOrders(Model model, Item item, HttpSession session){

        Integer userId =(Integer) session.getAttribute("userId");
        String itemName = item.getItemName();
        PageInfo<Order>pageInfo=orderService.listMyOrders(userId,itemName);
        model.addAttribute("pageInfo",pageInfo);
        return "myOrders-list";
    }

    @RequestMapping("delMyOrder/{id}")
    public String delMyOrder(@PathVariable Integer id){
        boolean b = orderService.removeById(id);
        return "redirect:/normalUser/myOrders";
    }

    @RequestMapping("preUpdateMyOrder/{id}")
    public String preUpdateMyOrder(@PathVariable Integer id,Model model){
        Order order = orderService.getById(id);
        Integer cid = order.getCid();
        Integer iid = order.getIid();
        Customer customer = customerService.getById(cid);
        Item item = itemService.getById(iid);
        model.addAttribute("item",item);
        model.addAttribute("customer",customer);
        model.addAttribute("order",order);
        return "myOrders-update";
    }

    @RequestMapping("updateMyOrder")
    public String updateMyOrder(Integer id,String address, String note){
        Order order = orderService.getById(id);
        order.setAddress(address);
        order.setNote(note);
        orderService.updateById(order);
        return "redirect:/normalUser/myOrders";
    }

    //批量删除
    @PostMapping("batchDeleteMyOrder")
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

    @RequestMapping("cart/{sid}")
    public String cart(Model model,HttpSession session,@PathVariable Integer sid,
                       @RequestParam(required = false,defaultValue = "1",value = "pageNum") Integer pageNum,
                       @RequestParam(required = false,defaultValue = "10",value = "pageSize") Integer pageSize){
        if (pageNum <= 0 || pageNum.equals("") || pageNum == null) {
            pageNum = 1;
        }
        if (pageSize <= 0 || pageSize.equals("") || pageSize == null) {
            pageSize = 10;
        }
        Integer userId =(Integer) session.getAttribute("userId");
        double a=0;
        List<CartItem> list = cartitemService.myCart(userId,sid);
        for (CartItem cartitem : list) {
            QueryWrapper<Item> qw=new QueryWrapper<>();
            qw.eq("id",cartitem.getIid());
            Item one = itemService.getOne(qw);
            a+=cartitem.getCount()*one.getPrice();
        }
        PageHelper.startPage(pageNum, pageSize);

        PageInfo<CartItem>pageInfo=new PageInfo<>(list);
        model.addAttribute("listsize",list.size());
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("totalMoney",a);
        model.addAttribute("storeId",sid);
        return "cart";
    }

    @RequestMapping("updateCartItemCount/{sid}/{iid}")
    public String updateCartItemCount(@PathVariable Integer sid,@PathVariable  Integer iid, Integer count, HttpServletRequest request){
        Integer userId  = (Integer) request.getSession().getAttribute("userId");
        QueryWrapper<CartItem> qw=new QueryWrapper<>();
        qw.eq("iid",iid);
        qw.eq("cid",userId);
        CartItem cartitem = cartitemService.getOne(qw);
        Integer oldCount = cartitem.getCount();
        cartitem.setCount(count);
        int b = cartitemService.updateCartItemCount(userId,cartitem.getIid(),count);
        //修改库存
        Item item = itemService.getById(iid);
        log.info(oldCount+"");
        log.info(count+"");
        if (oldCount>count){
            item.setStock(item.getStock()+(oldCount-count));
            itemService.updateById(item);
        }else {
            item.setStock(item.getStock()-(count-oldCount));
            itemService.updateById(item);
        }

        if (b==1){
            return "redirect:/normalUser/cart/"+Integer.toString(sid);
        }else {
            return "error";
        }

    }

    @RequestMapping("updateCartItemNote/{sid}/{iid}")
    public String updateCartItemNote(@PathVariable Integer sid,@PathVariable  Integer iid, String note, HttpServletRequest request){
        Integer userId  = (Integer) request.getSession().getAttribute("userId");
        QueryWrapper<CartItem> qw=new QueryWrapper<>();
        qw.eq("iid",iid);
        qw.eq("cid",userId);
        CartItem cartitem = cartitemService.getOne(qw);
        cartitem.setNote(note);
        int b = cartitemService.updateCartItemNote(userId,cartitem.getIid(),note);
        if (b==1){
            return "redirect:/normalUser/cart/"+Integer.toString(sid);
        }else {
            return "error";
        }
    }

    @RequestMapping("orderstatusfilter/{statusid}")
    public String orderStatusFilter(@PathVariable Integer statusid,HttpServletRequest request, Model model){

        Object userId=request.getSession().getAttribute("userId");
        PageInfo<Order> list = orderService.filtermyOrderbyStatus((Integer)userId,statusid);
        model.addAttribute("pageInfo",list);
        return "myOrders-list";
    }

    @RequestMapping("payOrder/{sid}")
    public String payOrder(HttpSession session,@PathVariable Integer sid, HttpServletRequest request){
        Integer userId = (Integer) session.getAttribute("userId");;
        QueryWrapper<Customer> qw = new QueryWrapper<>();
        qw.eq("id", userId);
        Customer cust = customerService.getOne(qw);
        PageInfo<CartItem> pageInfo=cartitemService.payOrder(userId,sid);
        List<CartItem> list = pageInfo.getList();
        double totalprice;

        for (CartItem cartitem : list) {
            Order order = new Order();
            order.setIid(cartitem.getIid());
            order.setCid(userId);
            order.setSid(sid);
            order.setCount(cartitem.getCount());
            order.setAddress(cust.getAddress());
            order.setNote(cartitem.getNote());
            order.setPhone(cust.getPhone());
            QueryWrapper<Item> qw1=new QueryWrapper<>();
            qw1.eq("id",cartitem.getIid());
            Item one=itemService.getOne(qw1);
            totalprice=cartitem.getCount()*one.getPrice();
            order.setTotal(totalprice);
            orderService.save(order);
        }

        int i=cartitemService.clearAllCartItem(userId,sid);

        return "redirect:/normalUser/myOrders";
    }

    @RequestMapping("toItemSingle/{itemId}")
    public String toItemSingle(@PathVariable  Integer itemId,Model model){
        Item item = itemService.getById(itemId);
        model.addAttribute("item",item);
        QueryWrapper<Item>qw=new QueryWrapper<>();
        qw.eq("sid",item.getSid());
        qw.eq("status",1);
        List<Item> list = itemService.list(qw);
        model.addAttribute("itemList",list);
        return "item-single";
    }

    @RequestMapping("itemToCart/{sid}/{itemId}")
    public String itemToCart(@PathVariable Integer itemId,@PathVariable Integer sid, HttpServletRequest request,HttpSession session){

        Integer userId =(Integer) request.getSession().getAttribute("userId");
        QueryWrapper<CartItem>qw=new QueryWrapper<>();
        qw.eq("cid",userId);
        qw.eq("iid",itemId);
        CartItem one = cartitemService.getOne(qw);
        if (one!=null){
            session.setAttribute("msg","此菜品已加入购物车,请到购物车更改数量");
            return "redirect:/normalUser/viewstore/"+Integer.toString(sid);
        }
        int b= cartitemService.addCartItem(userId,itemId,1);
        session.removeAttribute("msg");
        return "redirect:/normalUser/cart/"+Integer.toString(sid);
    }

    @RequestMapping("delCart/{sid}/{iid}")
    public String delCart(@PathVariable Integer sid,@PathVariable Integer iid,HttpSession session){

        boolean remove = cartitemService.removeById(iid);
        return "redirect:/normalUser/cart/"+Integer.toString(sid);
    }


    @RequestMapping("confirmOrder/{orderId}")
    public String confirmOrder(@PathVariable Integer orderId){
        Order order = orderService.getById(orderId);
        order.setStatus(6);
        orderService.updateById(order);
        return "redirect:/normalUser/myOrders";
    }

    @RequestMapping("invoice/{orderId}")
    public String invoice(@PathVariable Integer orderId,Model model){
        Order order = orderService.getInvoiceById(orderId);
        model.addAttribute("order",order);
        return "invoice";

    }



}
