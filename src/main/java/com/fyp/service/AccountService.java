package com.fyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fyp.pojo.User;

public interface AccountService extends IService<User> {
    boolean login(String user,String password);

}
