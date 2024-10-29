package com.fyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.fyp.pojo.CartItem;

import java.util.List;

public interface CartItemService extends IService<CartItem> {

    int updateCartItemCount(Integer userId, Integer iid,Integer count);

    int updateCartItemNote(Integer userId, Integer iid,String note);

    int clearAllCartItem(Integer userId, Integer sid);

    List <CartItem> myCart(Integer userId, Integer sid);

    PageInfo<CartItem> payOrder(Integer userId, Integer sid);

    int addCartItem(Integer cid, Integer iid, Integer count);
}