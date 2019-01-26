/**
 * @author: maxu1
 * @date: 2019/1/26 12:28
 */

package com.miaoshaproject.controller;

import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author maxu
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/get")
	@ResponseBody
	public UserVO getUser(@RequestParam(name="id") Integer id) {
		// 根据用户的id获取用户的信息
		UserModel user = userService.getUser(id);
		// 将核心领域模型用户对象转化为可以供UI使用的viewObject
		return convertFromModel(user);
	}

	private UserVO convertFromModel(UserModel userModel) {
		if(userModel == null) {
			return null;
		}
		UserVO userVO = new UserVO();
		BeanUtils.copyProperties(userModel, userVO);
		return userVO;
	}
}
