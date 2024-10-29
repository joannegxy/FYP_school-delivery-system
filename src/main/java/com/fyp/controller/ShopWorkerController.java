package com.fyp.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.fyp.pojo.ShopWorker;
import com.fyp.pojo.Store;
import com.fyp.service.ShopWorkerService;
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
@RequestMapping("shopworker")
public class ShopWorkerController {
    @Value("${location}")
    private String location;
    @Autowired
    private ShopWorkerService shopworkerService;
    @Autowired
    private StoreService storeService;


    //查询所有用户
    @RequestMapping("listshopworker")
    public String listShopWorker(HttpServletRequest request,@RequestParam(required = false,defaultValue = "1",value = "pageNum") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10",value = "pageSize") Integer pageSize, Model model, ShopWorker shopworker){
        Object shopId = request.getSession().getAttribute("shopId");
        Store store=storeService.getById((Integer)shopId);
        model.addAttribute("store",store);

        if (pageNum <= 0 || pageNum.equals("") || pageNum == null) {
            pageNum = 1;
        }
        if (pageSize <= 0 || pageSize.equals("") || pageSize == null) {
            pageSize = 10;
        }
        PageHelper.startPage(pageNum, pageSize);
        QueryWrapper<ShopWorker> qw=new QueryWrapper<>();
        qw.eq("shop_id",shopId);
        List<ShopWorker> list = shopworkerService.list(qw);
        PageInfo<ShopWorker>pageInfo=new PageInfo<>(list);
        model.addAttribute("pageInfo",pageInfo);
        return "shopworker-list";
    }

    //跳转到添加用户的页面
    @RequestMapping("preSaveshopworker")
    public String preSaveBook(Model model, HttpServletRequest request) {
        Object shopId = request.getSession().getAttribute("shopId");
        Store store=storeService.getById((Integer)shopId);
        model.addAttribute("store",store);
        return "shopworker-save";
    }


    //添加用户
    @RequestMapping("saveShopworker")
    public String saveBook(HttpServletRequest request, ShopWorker shopworker, MultipartFile file, String pwd, String confirmpwd, String shopworkerloginid , Model model){
        Object shopId=request.getSession().getAttribute("shopId");
        Store store=storeService.getById((Integer)shopId);
        model.addAttribute("store",store);

        QueryWrapper<ShopWorker> qw=new QueryWrapper<>();
        if (shopworkerloginid!=null){
            qw.like("shopworkerloginid",shopworkerloginid);
        }
        List<ShopWorker>list=shopworkerService.list(qw);
        if(!list.isEmpty()){
            model.addAttribute("msg","用户名已被占用，请选择另一个用户名");
            return "shopworker-save";
        }
        String s="";
        if(!pwd.equals(s)){
            if(pwd.equals(confirmpwd)){
                transFile(shopworker,file);
                shopworker.setPassword(DigestUtil.md5Hex(pwd));
                boolean save = shopworkerService.save(shopworker);
                return "redirect:/shopworker/listshopworker";
            }
            else{
                model.addAttribute("msg","密码和确认密码不同，请重新注册");
                return "shopworker-save";
            }
        }
        else{
            model.addAttribute("msg","密码不能为空，请重新注册");
            return "shopworker-save";
        }
    }

    //文件上传
    private void transFile(ShopWorker shopworker, MultipartFile file) {
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
        shopworker.setCimage(path);
    }

    //根据id获取用户
    @RequestMapping("preUpdateShopWorker/{id}")
    public String preUpdateBook(@PathVariable("id")Integer id,Model model){
        ShopWorker byId = shopworkerService.getById(id);
        model.addAttribute("shopworker",byId);
        return "shopworker-update";
    }

    //修改用户
    @RequestMapping("updateShopWorker")
    public String updateBook(ShopWorker shopworker){
        boolean save = shopworkerService.updateById(shopworker);
        return "redirect:/shopworker/listshopworker";

    }

    //删除用户
    @RequestMapping("delShopWorker/{id}")
    public String delBook(@PathVariable("id") Integer id){
        boolean b = shopworkerService.removeById(id);
        return "redirect:/shopworker/listshopworker";
    }

    //批量删除用户
    @PostMapping("batchDeleteShopWorker")
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
        boolean b = shopworkerService.removeByIds(list);
        if (b){
            return "OK";
        }else {
            return "error";
        }
    }
}
