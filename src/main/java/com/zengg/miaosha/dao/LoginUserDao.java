package com.zengg.miaosha.dao;


import com.zengg.miaosha.model.MiaoshaUser;
import org.apache.ibatis.annotations.*;


@Mapper
public interface LoginUserDao {

    @Select("select * from login_user where mobile = #{mobile}")
    public MiaoshaUser getByMobile(@Param("mobile")long mobile);


    @Update("update login_user set password = #{password} where mobile = #{mobile}")
    public int updateMiaoshaUserPassword(MiaoshaUser user);
}
