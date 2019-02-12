package com.zengg.miaosha.config.exception;

import com.zengg.miaosha.utils.CodeMsg;
import lombok.Getter;

/**
 * @program: miaosha
 * @description: 全局异常
 * @author: ZengGuangfu
 * @create 2019-02-12 09:01
 */
@Getter
public class GlobalException  extends  Exception{

    public static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.cm = cm ;
    }

}
