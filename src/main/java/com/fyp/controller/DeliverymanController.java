package com.fyp.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.fyp.pojo.*;
import com.fyp.service.DeliverymanService;
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
@RequestMapping("deliveryman")
public class DeliverymanController {
    @Value("${location}")
    private String location;
    @Autowired
    private DeliverymanService deliverymanService;
    @Autowired
    private StoreService storeService;

    //查询所有用户
    @RequestMapping("listdeliveryman")
    public String listDeliveryman(HttpServletRequest request, Model model, Deliveryman deliveryman){
        Object shopId = request.getSession().getAttribute("shopId");
        Store store=storeService.getById((Integer)shopId);
        model.addAttribute("store",store);
        QueryWrapper<Deliveryman> qw=new QueryWrapper<>();
        qw.eq("shop_id",shopId);
        List<Deliveryman> list = deliverymanService.list(qw);
        PageInfo<Deliveryman>pageInfo=new PageInfo<>(list);
        model.addAttribute("pageInfo",pageInfo);
        return "deliveryman-list";
    }

    //跳转到添加用户的页面
    @RequestMapping("preSavedeliveryman")
    public String preSaveBook(Model model, HttpServletRequest request){
        Object shopId=request.getSession().getAttribute("shopId");
        Store store=storeService.getById((Integer)shopId);
        model.addAttribute("store",store);
        return "deliveryman-save";
    }

    //添加用户
    @RequestMapping("savedeliveryman")
    public String saveBook(HttpServletRequest request, Deliveryman deliveryman, MultipartFile file, String pwd, String confirmpwd, String deliverymanloginid ,Model model){
        Object shopId=request.getSession().getAttribute("shopId");
        Store store=storeService.getById((Integer)shopId);
        model.addAttribute("store",store);
        QueryWrapper<Deliveryman> qw=new QueryWrapper<>();
        if (deliverymanloginid!=null){
            qw.like("deliverymanloginid",deliverymanloginid);
        }
        List<Deliveryman>list=deliverymanService.list(qw);
        if(!list.isEmpty()){
            model.addAttribute("msg","用户名已被占用，请选择另一个用户名");
            return "deliveryman-save";
        }
        String s="";
        if(!pwd.equals(s)){
            if(pwd.equals(confirmpwd)){
                transFile(deliveryman,file);
                deliveryman.setPassword(DigestUtil.md5Hex(pwd));
                boolean save = deliverymanService.save(deliveryman);
                return "redirect:/deliveryman/listdeliveryman";
            }
            else{
                model.addAttribute("msg","密码和确认密码不同，请重新注册");
                return "deliveryman-save";
            }
        }
        else{
            model.addAttribute("msg","密码不能为空，请重新注册");
            return "deliveryman-save";
        }
    }

    //文件上传
    private void transFile(Deliveryman deliveryman, MultipartFile file) {
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
        deliveryman.setCimage(path);
    }

    //根据id获取用户
    @RequestMapping("preUpdateDeliveryman/{id}")
    public String preUpdateBook(@PathVariable("id")Integer id,Model model){
        Deliveryman byId = deliverymanService.getById(id);
        model.addAttribute("deliveryman",byId);
        return "deliveryman-update";
    }

    //修改用户
    @RequestMapping("updateDeliveryman")
    public String updateBook(Deliveryman deliveryman){
        boolean save = deliverymanService.updateById(deliveryman);
        return "redirect:/deliveryman/listdeliveryman";

    }

    //删除用户
    @RequestMapping("delDeliveryman/{id}")
    public String delBook(@PathVariable("id") Integer id){
        boolean b = deliverymanService.removeById(id);
        return "redirect:/deliveryman/listdeliveryman";
    }

    //批量删除用户
    @PostMapping("batchDeleteDeliveryman")
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
        boolean b = deliverymanService.removeByIds(list);
        if (b){
            return "OK";
        }else {
            return "error";
        }
    }
}
