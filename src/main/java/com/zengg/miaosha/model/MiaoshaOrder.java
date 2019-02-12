package com.zengg.miaosha.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiaoshaOrder {

	private Long id;
	private Long userId;
	private Long  orderId;
	private Long goodsId;

}
