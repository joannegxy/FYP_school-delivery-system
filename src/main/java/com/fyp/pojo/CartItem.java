package com.fyp.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("cartitem")
public class CartItem {
    @TableId
    private Integer cid;
    private Integer iid;
    private Integer count;
    private String note;
    @TableField(exist = false)
    private Customer customer;
    @TableField(exist = false)
    private Item item;
}