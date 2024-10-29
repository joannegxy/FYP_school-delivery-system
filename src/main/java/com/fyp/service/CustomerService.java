package com.fyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fyp.pojo.Customer;

public interface CustomerService extends IService<Customer> {
    boolean login(String userName, String userPwd);
}
