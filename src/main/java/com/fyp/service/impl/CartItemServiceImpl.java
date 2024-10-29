package com.fyp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.fyp.mapper.CartItemMapper;
import com.fyp.pojo.CartItem;
import com.fyp.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemServiceImpl extends ServiceImpl<CartItemMapper, CartItem> implements CartItemService {

    @Autowired
    private CartItemMapper cartitemMapper;
    @Override
    public int updateCartItemCount(Integer userId, Integer iid,Integer count) {
        return cartitemMapper.updateCartItemCount(userId,iid,count);
    }

    @Override
    public int updateCartItemNote(Integer userId, Integer iid,String note) {
        return cartitemMapper.updateCartItemNote(userId,iid,note);
    }

    @Override
    public int clearAllCartItem(Integer userId, Integer sid){
        return cartitemMapper.clearAllCartItem(userId,sid);
    }

    @Override
    public List<CartItem> myCart(Integer userId, Integer sid) {
        List<CartItem> list=cartitemMapper.myCart(userId,sid);
        return list;
    }

    @Override
    public PageInfo<CartItem> payOrder(Integer userId, Integer sid) {
        List<CartItem> list=cartitemMapper.payOrder(userId,sid);
        return new PageInfo<>(list);
    }

    @Override
    public int addCartItem(Integer cid, Integer iid, Integer count){
        return cartitemMapper.addCartItem(cid,iid,count);
    }
}