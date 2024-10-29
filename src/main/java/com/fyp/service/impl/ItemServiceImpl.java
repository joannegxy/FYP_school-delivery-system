package com.fyp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fyp.mapper.ItemMapper;
import com.fyp.pojo.CountNumber;
import com.fyp.pojo.Item;
import com.fyp.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl extends ServiceImpl<ItemMapper, Item> implements ItemService {

    @Autowired
    private ItemMapper itemMapper;


    @Override
    public List<CountNumber> queryNum(Integer sid) {
        return itemMapper.queryNum(sid);
    }

    @Override
    public List<CountNumber> queryTotal(Integer sid) {
        return itemMapper.queryTotal(sid);
    }
}
