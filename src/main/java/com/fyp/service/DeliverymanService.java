package com.fyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fyp.pojo.Deliveryman;

public interface DeliverymanService extends IService<Deliveryman> {
    boolean login(String userName, String userPwd);
}
