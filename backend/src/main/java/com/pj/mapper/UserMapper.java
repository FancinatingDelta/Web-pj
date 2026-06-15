package com.pj.mapper;

import com.pj.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findByUsername(String username);
    int insert(User user);
}
