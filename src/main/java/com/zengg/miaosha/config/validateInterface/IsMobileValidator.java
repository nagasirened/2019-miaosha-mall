package com.zengg.miaosha.config.validateInterface;

import com.zengg.miaosha.utils.ValidatorUtil;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @program: miaosha
 * @description: 校验手机号
 * @author: ZengGuangfu
 * @create 2019-02-11 16:42
 */
public class IsMobileValidator implements ConstraintValidator<isMobile,String>{

    private boolean require = false;

    @Override
    public void initialize(isMobile isMobile) {
        require = isMobile.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (require){
            return ValidatorUtil.isMobile(value);
        }else{
            if (StringUtils.isEmpty(value)){
                return true;
            }else{
                return ValidatorUtil.isMobile(value);
            }
        }
    }
}
