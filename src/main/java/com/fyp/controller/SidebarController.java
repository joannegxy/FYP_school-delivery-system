package com.fyp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fyp.pojo.Store;
import com.fyp.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Component
public abstract class SidebarController {

    @Autowired
    private StoreService storeService;

    @ModelAttribute("allstorelist")
    public List<Store> allStoreList(Model model, Store store){
        QueryWrapper<Store> qw=new QueryWrapper<>();
        if (store.getStoreName()!=null){
            qw.like("store_name",store.getStoreName());
        }

        List<Store> allstoreList = storeService.list(qw);
        return allstoreList;
    }

}