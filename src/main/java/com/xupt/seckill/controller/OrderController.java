/**
 * @author: maxu1
 * @date: 2019/1/27 18:58
 */

package com.xupt.seckill.controller;

import com.xupt.seckill.error.BusinessException;
import com.xupt.seckill.error.EmBusinessError;
import com.xupt.seckill.response.CommonReturnType;
import com.xupt.seckill.service.OrderService;
import com.xupt.seckill.service.model.OrderModel;
import com.xupt.seckill.until.SysConstant;
import com.xupt.seckill.vo.UserVO;
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
                                        @RequestParam(name = "promoId") Integer promoId,
                                        HttpSession session) throws BusinessException {

        // 获取用户的登陆信息
        Boolean isLogin = (Boolean) session.getAttribute(SysConstant.IS_LOGIN);
        if (isLogin == null || !isLogin.booleanValue()) {
            throw new BusinessException(EmBusinessError.USER_NOT_LOGIN);
        }
        // 获取用户的信息
        UserVO userVO = (UserVO) session.getAttribute(SysConstant.LOGIN_USER);
        OrderModel orderModel = orderService.createOrder(promoId, userVO.getId(), itemId, amount);
        return CommonReturnType.create(orderModel);

    }
}
