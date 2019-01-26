/**
 * @author: maxu1
 * @date: 2019/1/26 12:30
 */

package com.miaoshaproject.service;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.service.model.UserModel;

/**
 *
 * @author maxu
 */
public interface UserService {
	/**
	 * 通过用户id获取用户的方法
 	 */
	UserModel getUser(Integer id);

	void register(UserModel userModel) throws BusinessException;

	UserModel validateLogin(String telPhone, String password) throws BusinessException;
}
