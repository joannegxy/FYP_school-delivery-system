package com.fyp.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.fyp.pojo.Customer;
import com.fyp.service.CustomerService;
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
@RequestMapping("customer")
public class CustomerController {
    @Value("${location}")
    private String location;
    @Autowired
    private CustomerService customerService;


    //查询所有用户
    @RequestMapping("listCustomer")
    public String listCustomer(Model model, Customer customer){
        QueryWrapper<Customer> qw=new QueryWrapper<>();
        if (customer.getCustomerloginid()!=null){
            qw.like("customerloginid",customer.getCustomerloginid());
        }
        List<Customer> list = customerService.list(qw);
        PageInfo<Customer>pageInfo=new PageInfo<>(list);
        model.addAttribute("pageInfo",pageInfo);
        return "customer-list";
    }

    //跳转到添加用户的页面
    @RequestMapping("preSaveCustomer")
    public String preSaveBook(){
        return "customer-save";
    }

    //添加用户
    @RequestMapping("saveCustomer")
    public String saveBook(Customer customer, MultipartFile file, String pwd, String confirmpwd, String customerloginid ,Model model){
        QueryWrapper<Customer> qw=new QueryWrapper<>();
        if (customerloginid!=null){
            qw.like("customerloginid",customerloginid);
        }
        List<Customer>list=customerService.list(qw);
        if(!list.isEmpty()){
            model.addAttribute("msg","用户名已被占用，请选择另一个用户名");
            return "customer-save";
        }
        String s="";
        if(!pwd.equals(s)){
            if(pwd.equals(confirmpwd)){
                transFile(customer,file);
                customer.setPassword(DigestUtil.md5Hex(pwd));
                boolean save = customerService.save(customer);
                return "redirect:/customer/listCustomer";
            }
            else{
                model.addAttribute("msg","密码和确认密码不同，请重新注册");
                return "customer-save";
            }
        }
        else{
            model.addAttribute("msg","密码不能为空，请重新注册");
            return "customer-save";
        }
    }

    //文件上传
    private void transFile(Customer customer, MultipartFile file) {
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
        customer.setCimage(path);
    }

    //根据id获取用户
    @RequestMapping("preUpdateCustomer/{id}")
    public String preUpdateBook(@PathVariable("id")Integer id,Model model){
        Customer byId = customerService.getById(id);
        model.addAttribute("customer",byId);
        return "customer-update";
    }

    //修改用户
    @RequestMapping("updateCustomer")
    public String updateBook(Customer customer){
        boolean save = customerService.updateById(customer);
        return "redirect:/customer/listCustomer";

    }

    //删除用户
    @RequestMapping("delCustomer/{id}")
    public String delBook(@PathVariable("id") Integer id){
        boolean b = customerService.removeById(id);
        return "redirect:/customer/listCustomer";
    }

    //批量删除用户
    @PostMapping("batchDeleteCustomer")
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
        boolean b = customerService.removeByIds(list);
        if (b){
            return "OK";
        }else {
            return "error";
        }
    }
}
