/**
 * @author: maxu1
 * @date: 2019/1/26 16:32
 */

package com.miaoshaproject.controller;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author maxu
 */
public class BaseController {
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Object handlerException(HttpServletRequest request, Exception ex) {

		Map responseData = new HashMap();
		if (ex instanceof BusinessException) {
			BusinessException businessException = (BusinessException) ex;
			responseData.put("errCode", businessException.getErrorCode());
			responseData.put("errMsg", businessException.getErrorMsg());
		} else {
			responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrorCode());
			responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrorMsg());
		}
		return CommonReturnType.create(responseData, "fail");
	}

}
