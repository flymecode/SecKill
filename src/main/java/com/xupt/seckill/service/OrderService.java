package com.xupt.seckill.service;

import com.xupt.seckill.error.BusinessException;
import com.xupt.seckill.service.model.OrderModel;

/**
 * @author maxu
 */
public interface OrderService {

	// 通过前端url传来的promoId,在下单接口内校验对应的id是否属于对应商品且活动已开始
	OrderModel createOrder(Integer promoId, Integer userId, Integer itemId, Integer amount, Integer stockLog) throws BusinessException;
}
