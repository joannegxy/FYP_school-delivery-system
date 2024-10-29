package com.fyp.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.fyp.pojo.Item;
import com.fyp.pojo.Store;
import com.fyp.service.ItemService;
import com.fyp.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("item")
public class ItemController {
    @Autowired
    private ItemService itemService;

    @Value("${location}")
    private String location;

    @Autowired
    private StoreService storeService;

    //查询所有商品
    @RequestMapping("listItem")
    public String listProduct(HttpServletRequest request, Model model, Item item){

        Object shopId = request.getSession().getAttribute("shopId");
        Store store=storeService.getById((Integer)shopId);
        model.addAttribute("store",store);
        QueryWrapper<Item> qw=new QueryWrapper<>();
        qw.eq("sid",store.getId());

        List<Item> list = itemService.list(qw);
        PageInfo<Item>pageInfo=new PageInfo<>(list);
        model.addAttribute("pageInfo",pageInfo);
        List<Store> storeList = storeService.list(null);
        model.addAttribute("storeList",storeList);

        return "item-list";
    }

    //跳转到添加商品的页面
    @RequestMapping("preSaveItem")
    public String preSaveProduct(Model model,HttpServletRequest request){
        Object shopId=request.getSession().getAttribute("shopId");
        Store store=storeService.getById((Integer)shopId);
        model.addAttribute("store",store);
        return "item-save";
    }

    //添加商品
    @RequestMapping("saveItem")
    public String saveBook(Item item, MultipartFile file){
        transFile(item,file);
        boolean save = itemService.save(item);
        return "redirect:/item/listItem";
    }

    private void transFile(Item item, MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index);
        String prefix =System.nanoTime()+"";
        String path=prefix+suffix;
        File file1 = new File(location);
        if (!file1.exists()){
            file1.mkdirs();
        }
        File file2 = new File(file1,path);
        try {
            file.transferTo(file2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        item.setFimage(path);
    }

    @RequestMapping("preUpdateItem/{id}")
    public String preUpdateItem(@PathVariable Integer id,Model model){
        Item byId = itemService.getById(id);
        model.addAttribute("item",byId);
        List<Store> list = storeService.list(null);
        model.addAttribute("storeList",list);
        return "item-update";
    }

    @RequestMapping("updateItem")
    public String updateItem(Item item){
        boolean b = itemService.updateById(item);
        return "redirect:/item/listItem";
    }

    @RequestMapping("shangjia/{id}")
    public String shangjia(@PathVariable Integer id){
        Item byId = itemService.getById(id);
        byId.setStatus(1);
        boolean b = itemService.updateById(byId);
        return "redirect:/item/listItem";
    }

    @RequestMapping("xiajia/{id}")
    public String xiajia(@PathVariable Integer id){
        Item byId = itemService.getById(id);
        byId.setStatus(2);
        boolean b = itemService.updateById(byId);
        return "redirect:/item/listItem";
    }

    //删除商品
    @RequestMapping("delItem/{id}")
    public String delBook(@PathVariable("id") Integer id){
        boolean b = itemService.removeById(id);
        return "redirect:/item/listItem";
    }

    //批量删除
    @PostMapping("batchDeleteItem")
    @ResponseBody
    public String batchDeleteBook(String idList){
        String[]split= StrUtil.split(idList,",");
        List<Integer>list=new ArrayList<>();

        for (String s : split) {
            if (!s.isEmpty()){
                int i = Integer.parseInt(s);
                list.add(i);
            }
        }
        boolean b = itemService.removeByIds(list);
        if (b){
            return "OK";
        }else {
            return "error";
        }
    }
}
