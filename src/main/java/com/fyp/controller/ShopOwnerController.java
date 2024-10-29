package com.fyp.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("shopowner")
public class ShopOwnerController {
    @Value("${location}")
    private String location;
    @Autowired
    private ShopOwnerService shopownerService;
    @Autowired
    private StoreService storeService;

    //查询所有用户
    @RequestMapping("listshopowner")
    public String listShopOwner(@RequestParam(required = false,defaultValue = "1",value = "pageNum") Integer pageNum,
                                 @RequestParam(required = false,defaultValue = "10",value = "pageSize") Integer pageSize, Model model, ShopOwner shopowner){
        if (pageNum <= 0 || pageNum.equals("") || pageNum == null) {
            pageNum = 1;
        }
        if (pageSize <= 0 || pageSize.equals("") || pageSize == null) {
            pageSize = 10;
        }

        List<ShopOwner> list = shopownerService.listShopOwners();
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<ShopOwner>pageInfo=new PageInfo<>(list);
        model.addAttribute("pageInfo",pageInfo);
        return "shopowner-list";
    }

    //跳转到添加用户的页面
    @RequestMapping("preSaveshopowner")
    public String preSaveBook(Model model){

        List<Store> storeList = storeService.list(null);
        model.addAttribute("storeList",storeList);
        return "shopowner-Save";
    }

    //添加用户
    @RequestMapping("saveShopowner")
    public String saveBook(ShopOwner shopowner, MultipartFile file, String pwd, String confirmpwd, String shopownerloginid ,Model model){
        List<Store> storeList = storeService.list(null);
        model.addAttribute("storeList",storeList);

        QueryWrapper<ShopOwner> qw=new QueryWrapper<>();
        if (shopownerloginid!=null){
            qw.like("shopownerloginid",shopownerloginid);
        }
        List<ShopOwner>list=shopownerService.list(qw);
        if(!list.isEmpty()){
            model.addAttribute("msg","用户名已被占用，请选择另一个用户名");
            return "shopowner-save";
        }
        String s="";
        if(!pwd.equals(s)){
            if(pwd.equals(confirmpwd)){
                transFile(shopowner,file);
                shopowner.setPassword(DigestUtil.md5Hex(pwd));
                boolean save = shopownerService.save(shopowner);
                return "redirect:/shopowner/listshopowner";
            }
            else{
                model.addAttribute("msg","密码和确认密码不同，请重新注册");
                return "shopowner-save";
            }
        }
        else{
            model.addAttribute("msg","密码不能为空，请重新注册");
            return "shopowner-save";
        }
    }

    //文件上传
    private void transFile(ShopOwner shopowner, MultipartFile file) {
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
        shopowner.setCimage(path);
    }

    //根据id获取用户
    @RequestMapping("preUpdateShopOwner/{id}")
    public String preUpdateBook(@PathVariable("id")Integer id,Model model){
        ShopOwner byId = shopownerService.getById(id);
        model.addAttribute("shopowner",byId);
        return "shopowner-update";
    }

    //修改用户
    @RequestMapping("updateShopOwner")
    public String updateBook(ShopOwner shopowner){
        boolean save = shopownerService.updateById(shopowner);
        return "redirect:/shopowner/listshopowner";

    }

    //删除用户
    @RequestMapping("delShopOwner/{id}")
    public String delBook(@PathVariable("id") Integer id){
        boolean b = shopownerService.removeById(id);
        return "redirect:/shopowner/listshopowner";
    }

    //批量删除用户
    @PostMapping("batchDeleteShopOwner")
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
        boolean b = shopownerService.removeByIds(list);
        if (b){
            return "OK";
        }else {
            return "error";
        }
    }
}
