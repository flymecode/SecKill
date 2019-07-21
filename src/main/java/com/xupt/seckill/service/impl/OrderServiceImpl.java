/**
 * @author: maxu1
 * @date: 2019/1/27 16:44
 */

package com.xupt.seckill.service.impl;

import com.xupt.seckill.error.BusinessException;
import com.xupt.seckill.error.EmBusinessError;
import com.xupt.seckill.mapper.OrderMapper;
import com.xupt.seckill.mapper.StockLogMapper;
import com.xupt.seckill.pojo.OrderInfo;
import com.xupt.seckill.pojo.StockLog;
import com.xupt.seckill.service.ItemService;
import com.xupt.seckill.service.OrderService;
import com.xupt.seckill.service.UserService;
import com.xupt.seckill.service.model.ItemModel;
import com.xupt.seckill.service.model.OrderModel;
import com.xupt.seckill.until.OrderNo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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

	@Autowired
	private StockLogMapper stockLogMapper;

	@Override
	@Transactional // 只有等到整个方法返回的时候才会执行 commit
	public OrderModel createOrder(Integer promoId, Integer userId, Integer itemId, Integer amount, Integer stockLogId) throws BusinessException {
		// 校验下单状态，下单的商品是否存在，用户是否合法，购买的数量是否正确
		// ItemModel itemModel = itemService.getItemById(itemId);


		// 进行优化，首先从缓存中获取，减少了访问数据库的操作
		ItemModel itemModel = itemService.getItemByIdInCache(itemId);
		if (itemModel == null) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALDITION_ERROR, "商品信息不存在");
		}
		// 通过缓存获取用户对象
		// UserModel userInfo = userService.getUser(userId);
//		UserModel userInfo = userService.getUserFromCache(userId);
//		if (userInfo == null) {
//			throw new BusinessException(EmBusinessError.PARAMETER_VALDITION_ERROR, "用户信息不存在");
//		}
		if (amount <= 0 && amount > 99) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALDITION_ERROR, "数量信息不正确");
		}

		// 判断活动是否存在
//		if(promoId != null) {
//			// 判断对应的活动是否存在这个适用的商品
//			if (promoId.intValue() != itemModel.getPromoModel().getId()) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALDITION_ERROR, "活动信息不正确");
//			} else if(itemModel.getPromoModel().getStatus() != 2) {
//				throw new BusinessException(EmBusinessError.PARAMETER_VALDITION_ERROR, "活动还未开始");
//			}
//		}
		/**-----------------------------------------上面是校验------------------------------------------------------------*/
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
		if (promoId != null) {
			orderModel.setItemPrice(itemModel.getPromoModel().getPromoItemPrice());
		} else {
			orderModel.setItemPrice(itemModel.getPrice());
		}
		orderModel.setOrderPrice(itemModel.getPrice().multiply(new BigDecimal(amount)));
		orderModel.setPromoId(promoId);

		// 生成交易流水号
		orderModel.setId(orderNo.generateOrderNo());
		OrderInfo orderInfo = convertFromOrderModel(orderModel);
		orderMapper.insertSelective(orderInfo);
		// 加上商品的销量
		itemService.increaseSales(itemId,amount);

		// 设置库存流水状态为成功
		StockLog stockLog = stockLogMapper.selectById(stockLogId);
		if (stockLog == null ){
			throw new BusinessException(EmBusinessError.UNKNOWN_ERROR);
		}
		stockLog.setStatus(1);
		stockLogMapper.updateById(stockLog);

		// 为了解决commit失败，库存扣除的问题。
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                // TODO 在返回数据前我们进行同步库存的操作,最后发送
                boolean asyncDecreaseStock = itemService.asyncDecreaseStock(itemId, amount);
                // 使用 rabbitmq confirm 确认机制

            }
        });
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
