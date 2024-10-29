package com.fyp.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fyp.mapper.ShopWorkerMapper;
import com.fyp.pojo.ShopWorker;
import com.fyp.service.ShopWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ShopWorkerServiceImpl extends ServiceImpl<ShopWorkerMapper, ShopWorker>implements ShopWorkerService {
    @Autowired
    private ShopWorkerMapper shopworkerMapper;
    @Override
    public boolean login(String userName, String userPwd) {
        QueryWrapper<ShopWorker> qw=new QueryWrapper<>();
        qw.eq("shopworkerloginid",userName);
        ShopWorker shopworker = shopworkerMapper.selectOne(qw);
        if (shopworker==null){
            return false;
        }
        String userPassword = shopworker.getPassword();
        String s = DigestUtil.md5Hex(userPwd);
        if (Objects.equals(userPassword,s)){
            return true;
        }else {
            return false;
        }
    }
}