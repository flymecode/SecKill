/**
 * @author: maxu1
 * @date: 2019/1/26 12:32
 */

package com.miaoshaproject.service.impl;

import com.miaoshaproject.mapper.UserInfoMapper;
import com.miaoshaproject.mapper.UserPasswordMapper;
import com.miaoshaproject.pojo.UserInfo;
import com.miaoshaproject.pojo.UserPassword;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @author maxu
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Autowired
	private UserPasswordMapper userPasswordMapper;

	@Override
	public UserModel getUser(Integer id) {
		UserInfo userInfo = userInfoMapper.findUserById(id);
		if(userInfo == null) {
			return null;
		}
		UserPassword userPassword = userPasswordMapper.findUserPasswordByUserId(userInfo.getId());
		return transform(userInfo,userPassword);
	}

	private UserModel transform(UserInfo userInfo, UserPassword userPassword) {
		if(userInfo == null) {
			return null;
		}
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(userInfo,userModel);
		if(userPassword != null) {
			userModel.setEncrptPassword(userPassword.getEncrptPassword());
		}
		return userModel;
	}
}
