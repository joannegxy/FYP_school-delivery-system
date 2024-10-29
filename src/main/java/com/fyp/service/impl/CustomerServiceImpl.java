package com.fyp.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fyp.mapper.CustomerMapper;
import com.fyp.pojo.Customer;
import com.fyp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public boolean login(String userName, String userPwd) {
        QueryWrapper<Customer> qw=new QueryWrapper<>();
        qw.eq("customerloginid",userName);
        Customer customer = customerMapper.selectOne(qw);
        if (customer==null){
            return false;
        }
        String userPassword = customer.getPassword();
        String s = DigestUtil.md5Hex(userPwd);
        if (Objects.equals(userPassword,s)){
            return true;
        }else {
            return false;
        }
    }
}
