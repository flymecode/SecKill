/**
 * @author: maxu1
 * @date: 2019/1/27 16:32
 */

package com.xupt.seckill.service.model;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @author maxu
 */
@Data
public class OrderModel {
	// 交易号
	private String id;

	// 用户id
	private Integer userId;
	//如果不是空就是秒杀商品下单
	private Integer promoId;
	// 购买的商品id
	private Integer itemId;

	// 购买的数量
	private Integer amount;

	// 购买的金额，如果promoid不为空，则表示秒杀总金额
	private BigDecimal orderPrice;

	// 购买商品的单价，如果promoid不为空，则表秒杀单价
	private BigDecimal itemPrice;


}
