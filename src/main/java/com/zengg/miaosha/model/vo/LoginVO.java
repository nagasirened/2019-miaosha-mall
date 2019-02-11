package com.zengg.miaosha.model.vo;

import com.zengg.miaosha.config.validateInterface.isMobile;
import com.zengg.miaosha.utils.CodeMsg;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @program: miaosha
 * @description: 登录用
 * @author: ZengGuangfu
 * @create 2019-02-11 16:33
 */

@Data
public class LoginVO {

    @NotNull
    @isMobile
    private String mobile;

    @NotNull
    @Length(min = 32)
    private String password;
}
