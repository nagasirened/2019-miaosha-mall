package com.zengg.miaosha.model.vo;

import com.zengg.miaosha.model.MiaoshaUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: miaosha
 * @description: 加入秒杀队列的信息类
 * @author: ZengGuangfu
 * @create 2019-02-15 09:38
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiaoshaMessageVO {

    private MiaoshaUser miaoshaUser;

    private long goodsId;
}
