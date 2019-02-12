package com.zengg.miaosha.model;

import lombok.Data;

import java.util.Date;

/**
 * @program: miaosha
 * @description: 登录用户实体类
 * @author: ZengGuangfu
 * @create 2019-02-11 15:17
 */
@Data
public class MiaoshaUser {

    private long mobile;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private Date registerDate;
    private Date lastLoginDate;
    private Integer loginCount;

}
