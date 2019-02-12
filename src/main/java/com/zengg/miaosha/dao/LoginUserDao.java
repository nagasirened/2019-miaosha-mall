package com.zengg.miaosha.dao;


import com.zengg.miaosha.model.MiaoshaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LoginUserDao {

    @Select("select * from login_user where mobile = #{mobile}")
    public List<MiaoshaUser> getByMobile(@Param("mobile")long mobile);
}
