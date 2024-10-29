package com.fyp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fyp.pojo.CountNumber;
import com.fyp.pojo.Item;

import java.util.List;

public interface ItemService extends IService<Item> {


    List<CountNumber> queryNum(Integer sid);

    List<CountNumber> queryTotal(Integer sid);
}
