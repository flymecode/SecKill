package com.xupt.seckill.error;


/**
 * @author maxu
 */
public enum EmBusinessError implements CommonError {
	// 通用错误类型0001
	PARAMETER_VALDITION_ERROR(00001,"参数不合法"),
    // 1000x开头定义用户相关信息
	USER_NOT_EXSIT(10001,"用户不存在"),
	UNKNOWN_ERROR(20000,"未知错误"),
	STOCK_NOT_ENOUGH(30001,"库存不足"),
	USER_LOGIN_FAIL(20001,"用户或密码不正确"),
	USER_NOT_LOGIN(20002,"用户未登录") ;
	private int errorCode;
	private String errorMsg;

	EmBusinessError(int errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	@Override
	public int getErrorCode() {
		return this.errorCode;
	}

	@Override
	public String getErrorMsg() {
		return this.errorMsg;
	}

	@Override
	public CommonError setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
		return this;
	}
}
