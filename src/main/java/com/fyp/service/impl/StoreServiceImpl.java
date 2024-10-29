package com.fyp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fyp.mapper.StoreMapper;
import com.fyp.pojo.Store;
import com.fyp.service.StoreService;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store>implements StoreService {
}
