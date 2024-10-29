package com.fyp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fyp.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMapper extends BaseMapper<User> {
}
