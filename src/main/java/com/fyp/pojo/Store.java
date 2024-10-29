package com.fyp.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("store")
public class Store {
    @TableId
    private Integer id;
    private String storeName;
    private String descr;

    @TableField(exist = false)
    private String store_name;
}
