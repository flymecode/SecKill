/**
 * @author: maxu1
 * @date: 2019/1/27 18:58
 */

package com.miaoshaproject.controller;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.until.SysConstant;
import com.miaoshaproject.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author maxu
 */
@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {
	@Autowired
	private OrderService orderService;

	public CommonReturnType createOrder(@RequestParam(name = "itemId") Integer itemId,
	                                    @RequestParam(name = "amount") Integer amount,
	                                    HttpSession session) throws BusinessException {

		// 获取用户的登陆信息
		Boolean isLogin= (Boolean) session.getAttribute(SysConstant.IS_LOGIN);
		if (isLogin == null || !isLogin.booleanValue()) {
			throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
		}
		// 获取用户的信息
		UserVO userVO = (UserVO)session.getAttribute(SysConstant.LOGIN_USER);
		OrderModel orderModel = orderService.createOrder(userVO.getId(), itemId, amount);
		return CommonReturnType.create(orderModel);

	}
}
