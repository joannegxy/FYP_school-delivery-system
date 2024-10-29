package com.fyp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.fyp.pojo.Item;
import com.fyp.pojo.Store;
import com.fyp.service.ItemService;
import com.fyp.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("stock")
public class StockController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private StoreService storeService;

    //查询所有商品
    @RequestMapping("listStock")
    public String list(HttpServletRequest request, Model model, Item item) {
        Object shopId = request.getSession().getAttribute("shopId");
        Store store=storeService.getById((Integer)shopId);
        model.addAttribute("sid",shopId);
        QueryWrapper<Item>qw=new QueryWrapper<>();
        qw.eq("sid",store.getId());
        List<Item> list = itemService.list(qw);
        PageInfo<Item> objectPageInfo = new PageInfo<>(list);
        model.addAttribute("pageInfo", objectPageInfo);
        model.addAttribute("store",store.getStoreName());
        return "stock-list";
    }



    //添加库存
    @RequestMapping("addStock/{id}")
    public String addStock(@PathVariable  Integer id, int restockcount){
        Item byId = itemService.getById(id);
        byId.setStock(byId.getStock()+restockcount);
        itemService.updateById(byId);
        return "redirect:/stock/listStock";
    }
}
