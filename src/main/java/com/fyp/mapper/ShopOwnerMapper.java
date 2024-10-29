package com.fyp.mapper;

import com.fyp.pojo.ShopOwner;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopOwnerMapper extends BaseMapper<ShopOwner> {

    List<ShopOwner> listShopOwners();
}
