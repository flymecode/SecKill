/**
 * @author: maxu1
 * @date: 2019/1/26 12:32
 */

package com.miaoshaproject.service.impl;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.mapper.UserInfoMapper;
import com.miaoshaproject.mapper.UserPasswordMapper;
import com.miaoshaproject.pojo.UserInfo;
import com.miaoshaproject.pojo.UserPassword;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
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
		if (userInfo == null) {
			return null;
		}
		UserPassword userPassword = userPasswordMapper.findUserPasswordByUserId(userInfo.getId());
		return transform(userInfo, userPassword);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void register(UserModel userModel) throws BusinessException {
		if (StringUtils.isEmpty(userModel)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALDITION_ERROR);
		}
		if (StringUtils.isEmpty(userModel.getName())
				|| userModel.getGender() == null
				|| userModel.getAge() == null
				|| StringUtils.isEmpty(userModel.getTelPhone())) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALDITION_ERROR);
		}
		// 实现model转为dataobject的方法
		UserInfo userInfo = transform(userModel);
		try {
			userInfoMapper.insertSelective(userInfo);
		} catch (DuplicateKeyException ex) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALDITION_ERROR, "手机号已经存在");
		}

		UserPassword userPassword = convertPasswordFormModel(userModel);
		userPasswordMapper.insertSelective(userPassword);

	}

	@Override
	public UserModel validateLogin(String telPhone, String password) throws BusinessException {
		// 通过用户手机获取用户的信息
		UserInfo userInfo = userInfoMapper.findUserByTelphone(telPhone);
		if (userInfo == null) {
			throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
		}
		UserPassword userPassword = userPasswordMapper.findUserPasswordByUserId(userInfo.getId());
		UserModel userModel = transform(userInfo,userPassword);
		// 比对用户的信息内加密的密码是否和传输进来的密码相匹配
		if (com.alibaba.druid.util.StringUtils.equals(password, userPassword.getEncrptPassword())) {
			throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
		}
		return userModel;
	}

	//---------------转换方法-------------------------------------
	private UserPassword convertPasswordFormModel(UserModel userModel) {
		if (userModel == null) {
			return null;
		}
		UserPassword userPassword = new UserPassword();
		userPassword.setEncrptPassword(userModel.getEncrptPassword());
		userPassword.setUserId(userModel.getId());
		return userPassword;
	}


	private UserInfo transform(UserModel userModel) {
		if (userModel == null) {
			return null;
		}
		UserInfo userInfo = new UserInfo();
		BeanUtils.copyProperties(userInfo, userModel);
		return userInfo;

	}

	private UserModel transform(UserInfo userInfo, UserPassword userPassword) {
		if (userInfo == null) {
			return null;
		}
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(userInfo, userModel);
		if (userPassword != null) {
			userModel.setEncrptPassword(userPassword.getEncrptPassword());
		}
		return userModel;
	}
}
