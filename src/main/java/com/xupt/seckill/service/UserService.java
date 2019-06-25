/**
 * @author: maxu1
 * @date: 2019/1/26 12:30
 */

package com.xupt.seckill.service;

import com.xupt.seckill.error.BusinessException;
import com.xupt.seckill.service.model.UserModel;

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

	/**
	 * 从缓存中获取用户信息
	 * @param userId
	 * @return
	 */
    UserModel getUserFromCache(Integer userId);
}
