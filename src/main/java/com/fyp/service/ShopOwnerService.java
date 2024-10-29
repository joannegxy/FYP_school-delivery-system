package com.fyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fyp.pojo.ShopOwner;

import java.util.List;

public interface ShopOwnerService extends IService<ShopOwner> {

    boolean login(String userName, String userPwd);

    List <ShopOwner> listShopOwners();
}