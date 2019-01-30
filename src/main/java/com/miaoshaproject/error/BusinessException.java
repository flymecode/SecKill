/**
 * @author: maxu1
 * @date: 2019/1/26 15:35
 */

package com.miaoshaproject.error;

/**
 * 包装器异常处理,装饰器模式
 * @author maxu
 */
public class BusinessException extends Exception implements CommonError {

	private CommonError commonError;

	//直接接受
	public BusinessException(CommonError commonError) {
		super();
		this.commonError = commonError;
	}

	public BusinessException(CommonError commonError,String errMsg) {
		super();
		this.commonError.setErrorMsg(errMsg);
		this.commonError = commonError;
	}
	@Override
	public int getErrorCode() {
		return commonError.getErrorCode();
	}

	@Override
	public String getErrorMsg() {
		return commonError.getErrorMsg();
	}

	@Override
	public CommonError setErrorMsg(String errorMsg) {
		this.commonError.setErrorMsg(errorMsg);
		return this;
	}
}
