/**
 * @author: maxu1
 * @date: 2019/1/27 16:44
 */

package com.miaoshaproject.service.impl;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.mapper.OrderMapper;
import com.miaoshaproject.pojo.OrderInfo;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.OrderService;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.service.model.OrderModel;
import com.miaoshaproject.service.model.UserModel;
import com.miaoshaproject.until.OrderNo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author maxu
 */
@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrderNo orderNo;

	@Autowired
	@Resource
	private OrderMapper orderMapper;

	@Autowired
	@Resource
	private ItemService itemService;

	@Autowired
	@Resource
	private UserService userService;

	@Override
	@Transactional
	public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) throws BusinessException {
		// 校验下单状态，下单的商品是否存在，用户是否合法，购买的数量是否正确
		ItemModel itemModel = itemService.getItemById(itemId);
		if (itemModel == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALDITION_ERROR, "商品信息不存在");
		}
		UserModel userInfo = userService.getUser(userId);
		if (userInfo == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALDITION_ERROR, "用户信息不存在");
		}
		if (amount <= 0 && amount > 99) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALDITION_ERROR, "数量信息不正确");
		}
		// 落单减库存
		Boolean result = itemService.decreaseStock(itemId,amount);
		if (!result) {
			throw new BusinessException(EmBusinessError.STOCK_NOT_ENOUGH);
		}
		// 订单入库
		OrderModel orderModel = new OrderModel();
		orderModel.setUserId(userId);
		orderModel.setItemId(itemId);
		orderModel.setAmount(amount);
		orderModel.setItemPrice(itemModel.getPrice());
		orderModel.setOrderPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));

		// 生成交易流水号
		orderModel.setId(orderNo.generateOrderNo());
		OrderInfo orderInfo = convertFromOrderModel(orderModel);
		orderMapper.insertSelective(orderInfo);
		// 加上商品的销量
		itemService.increaseSales(itemId,amount);
		// 返回数据
		return orderModel;
	}

	private OrderInfo convertFromOrderModel(OrderModel orderModel) {
		if (orderModel == null) {
			return null;
		}
		OrderInfo orderInfo = new OrderInfo();
		BeanUtils.copyProperties(orderModel, orderInfo);
		orderInfo.setItemPrice(orderModel.getItemPrice());
		orderInfo.setOrderPrice(orderModel.getOrderPrice());
		return orderInfo;
	}
}
