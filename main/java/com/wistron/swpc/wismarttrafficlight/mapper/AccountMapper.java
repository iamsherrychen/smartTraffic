package com.wistron.swpc.wismarttrafficlight.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AccountMapper {

    int authAccount(String name, String pwd);

}
