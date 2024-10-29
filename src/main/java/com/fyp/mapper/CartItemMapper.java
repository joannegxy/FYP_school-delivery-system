package com.fyp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fyp.pojo.CartItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemMapper extends BaseMapper<CartItem> {

    int updateCartItemCount(Integer userId, Integer iid,Integer count);

    int updateCartItemNote(Integer userId, Integer iid,String note);

    int clearAllCartItem(Integer userId, Integer sid);

    List <CartItem> myCart(Integer userId, Integer sid);

    List<CartItem> payOrder(Integer userId, Integer sid);

    int addCartItem(Integer cid, Integer iid, Integer count);
}