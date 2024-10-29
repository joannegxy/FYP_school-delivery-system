package com.fyp.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("shopowner")
public class ShopOwner {
    @TableId
    private Integer id;
    private Integer shopId;
    private String shopownerloginid;
    private String password;
    private String email;
    private String phone;
    private String cimage;

    @TableField(exist = false)
    private Store store;

    @TableField(exist = false)
    private Integer shop_id;



}