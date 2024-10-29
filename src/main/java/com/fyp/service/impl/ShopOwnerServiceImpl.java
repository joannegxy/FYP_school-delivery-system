package com.fyp.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fyp.mapper.ShopOwnerMapper;
import com.fyp.pojo.ShopOwner;
import com.fyp.service.ShopOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ShopOwnerServiceImpl extends ServiceImpl<ShopOwnerMapper, ShopOwner>implements ShopOwnerService {
    @Autowired
    private ShopOwnerMapper shopownerMapper;
    @Override
    public boolean login(String userName, String userPwd) {
        QueryWrapper<ShopOwner> qw=new QueryWrapper<>();
        qw.eq("shopownerloginid",userName);
        ShopOwner shopowner = shopownerMapper.selectOne(qw);
        if (shopowner==null){
            return false;
        }
        String userPassword = shopowner.getPassword();
        String s = DigestUtil.md5Hex(userPwd);
        if (Objects.equals(userPassword,s)){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public List<ShopOwner> listShopOwners(){
        List<ShopOwner> list=shopownerMapper.listShopOwners();
        return list;
    }
}