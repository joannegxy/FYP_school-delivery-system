package com.fyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fyp.pojo.ShopWorker;

public interface ShopWorkerService extends IService<ShopWorker> {
    boolean login(String userName, String userPwd);
}