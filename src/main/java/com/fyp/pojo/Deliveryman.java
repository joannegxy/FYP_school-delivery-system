package com.fyp.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("deliveryman")
public class Deliveryman {
    @TableId
    private Integer id;
    private Integer shopId;
    private String deliverymanloginid;
    private String password;
    private String email;
    private String phone;
    private String cimage;
}