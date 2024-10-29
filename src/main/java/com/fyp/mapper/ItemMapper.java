package com.fyp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fyp.pojo.CountNumber;
import com.fyp.pojo.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemMapper extends BaseMapper<Item> {

    List<CountNumber> queryNum(Integer sid);

    List<CountNumber> queryTotal(Integer sid);
}
