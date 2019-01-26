/**
 * @author: maxu1
 * @date: 2019/1/26 12:30
 */

package com.miaoshaproject.service;

import com.miaoshaproject.service.model.UserModel;

/**
 *
 * @author maxu
 */
public interface UserService {
	// 通过用户id获取用户的方法
	UserModel getUser(Integer id);
}
