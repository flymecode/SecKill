/**
 * @author: maxu1
 * @date: 2019/1/26 12:28
 */

package com.miaoshaproject.controller;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.vo.UserVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;

/**
 * @author maxu
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController{

	@Autowired
	@NotNull
	private UserService userService;

	@ApiOperation(value = "获取用户信息", notes = "根据url的id来获取用户详细信息")
	@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "path")
	@GetMapping("/{id}")
	@ResponseBody
	public CommonReturnType getUser(@PathVariable("id") Integer id) throws BusinessException {
		// 根据用户的id获取用户的信息
		UserModel user = userService.getUser(id);
		if (StringUtils.isEmpty(user)) {
			throw new BusinessException(EmBusinessError.USER_NOT_EXSIT);
		}
		// 将核心领域模型用户对象转化为可以供UI使用的viewObject
		UserVO userVO = convertFromModel(user);
		return CommonReturnType.create(userVO);
	}

	private UserVO convertFromModel(UserModel userModel) {
		if (userModel == null) {
			return null;
		}
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(userModel, userVO);
		return userVO;
	}

}
