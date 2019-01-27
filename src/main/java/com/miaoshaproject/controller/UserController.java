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
import com.miaoshaproject.until.EncodeByMd5;
import com.miaoshaproject.vo.UserVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author maxu
 */
@Controller
@RequestMapping("/user")
@Slf4j
// 解决session共享
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
// allowCredentials xhrFields 授信后使得跨域session共享
// allowedHeaders允许跨域传输所有的header参数，将用于使用token放入header域做session共享的跨域请求。
public class UserController extends BaseController {

	@Autowired
	@NotNull
	private UserService userService;



	@PostMapping("/login")
	@ResponseBody
	public CommonReturnType login(@RequestParam(name = "telphone") String telPhone,
	                              @RequestParam(name = "password") String password,
	HttpSession session) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
		if (StringUtils.isEmpty(telPhone) || StringUtils.isEmpty(password)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALDITION_ERROR);
		}
		// 用户登陆服务,用来验证用户是否登陆合法
		UserModel userModel = userService.validateLogin(telPhone, EncodeByMd5.encode(password));
		// 将登陆凭证加入到用户登陆成功的session内
		UserVO userVO = convertFromModel(userModel);
		session.setAttribute("IS_LOGIN",true);
		session.setAttribute("LOGIN_USER",userVO);
		return CommonReturnType.create(userVO);
	}



	@ApiOperation(value = "用户注册接口")
	@PostMapping("/register")
	@ResponseBody
	public CommonReturnType register(UserVO userVO,
	                                 @RequestParam("password") String password,
	                                 @RequestParam("otpCode") String optCode,
	                                 HttpSession session) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
		//验证手机号对应的otpCode
		String inSessionOptCode = (String) session.getAttribute(userVO.getTelPhone());
		if (com.alibaba.druid.util.StringUtils.equals(optCode, inSessionOptCode)) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALDITION_ERROR, "短信验证码不符合");
		}
		// 用户注册流程
		UserModel userModel = new UserModel();
		BeanUtils.copyProperties(userModel, userVO);
		userModel.setThirdPartyId(1);
		// TODO 盐值
		userModel.setEncrptPassword(EncodeByMd5.encode(password));
		userService.register(userModel);
		return CommonReturnType.create(null);
	}

	@ApiOperation(value = "获取otp短信接口")
	@ApiImplicitParam(name = "telPhone", value = "电话号码", required = true, dataType = "int", paramType = "query")
	@PostMapping("/otp")
	@ResponseBody
	public CommonReturnType getOtp(@RequestParam("telPhone") String telPhone, HttpSession session) {
		// 按照一定的规则生成opt验证码
		Random random = new Random(7);
		int randomInt = random.nextInt(99999);
		randomInt += 10000;
		String optCode = String.valueOf(randomInt);
		// 将otp验证码与用户关联
		session.setAttribute(telPhone, optCode);
		// 将otp验证码发送给用户
		return CommonReturnType.create(null);
	}

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
