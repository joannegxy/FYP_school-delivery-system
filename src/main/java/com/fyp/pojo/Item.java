package com.fyp.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("item")
public class Item {
    @TableId
    private Integer id;
    private Integer sid;
    private String itemName;
    private Double price;
    private Integer stock;
    private String descr;
    private String fimage;
    private Integer status;

    @TableField(exist = false)
    private Store store;

}
