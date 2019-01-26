/**
 * @author: maxu1
 * @date: 2019/1/26 15:03
 */

package com.miaoshaproject.response;

import lombok.Data;

/**
 * 统一返回实体
 * @author maxu
 */
@Data
public class CommonReturnType {

	// 表明对应请求的处理结果 success 或者 fail
	private String status;
	// 如果 status = suceess 返回前端需要的请求信息
	// 如果 status = fail 返回通用的错误码格式
	private Object data;

	public static CommonReturnType create(Object result) {
		return  CommonReturnType.create(result,"success");
	}
	public static CommonReturnType create(Object result,String status) {
		CommonReturnType type = new CommonReturnType();
		type.setData(result);
		type.setStatus(status);
		return type;
	}
}
