package com.fyp.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fyp.mapper.DeliverymanMapper;
import com.fyp.pojo.Deliveryman;
import com.fyp.service.DeliverymanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DeliverymanServiceImpl extends ServiceImpl<DeliverymanMapper, Deliveryman>implements DeliverymanService {
    @Autowired
    private DeliverymanMapper deliverymanMapper;
    @Override
    public boolean login(String userName, String userPwd) {
        QueryWrapper<Deliveryman> qw=new QueryWrapper<>();
        qw.eq("deliverymanloginid",userName);
        Deliveryman deliveryman = deliverymanMapper.selectOne(qw);
        if (deliveryman==null){
            return false;
        }
        String userPassword = deliveryman.getPassword();
        String s = DigestUtil.md5Hex(userPwd);
        if (Objects.equals(userPassword,s)){
            return true;
        }else {
            return false;
        }
    }
}