package com.zengg.miaosha.utils;

public class CodeMsg {
	private int code;
	private String msg;
	
	//通用异常
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");


	public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
	public static CodeMsg VALIDATION_ERROR = new CodeMsg(500101, "参数校验异常：%s");
	public static CodeMsg GLOBAL_ERROR = new CodeMsg(500101, "全局异常：%s");
	//登录模块 5002XX
	public static CodeMsg SESSION_ERROR = new CodeMsg(500201, "session不存在或失效，请重新登录");
	public static CodeMsg PASSWPRD_EMPTY = new CodeMsg(500201, "密码不能为空");
	public static CodeMsg MOBILE_EMPTY = new CodeMsg(500202, "手机号不能为空");
	public static CodeMsg MOBILE_ERROR = new CodeMsg(500203, "请输入正确的手机号");
	public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500204, "用户不存在");
	public static CodeMsg PASSWORD_ERROR = new CodeMsg(500205, "密码错误");
	//商品模块 5003XX
	
	//订单模块 5004XX
	public static CodeMsg ORDER_ERROR = new CodeMsg(500401, "订单号不能为空");
	public static CodeMsg ORDER_NOT_EXIT = new CodeMsg(500402, "订单不存在");

	//秒杀模块 5005XX
	public static CodeMsg MIAOSHA_OVER = new CodeMsg(500501, "库存不足");
	public static CodeMsg MIAOSHA_REPET = new CodeMsg(500501, "不能重复秒杀");

	private CodeMsg(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public CodeMsg fillmsg(Object... args) {
		int code = this.code;
		String message = String.format(this.msg, args);
		return new CodeMsg(code,message);
	}
	
	public int getCode() {
		return code;
	}
	public String getMsg() {
		return msg;
	}
}
