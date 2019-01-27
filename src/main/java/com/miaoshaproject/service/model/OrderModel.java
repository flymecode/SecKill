/**
 * @author: maxu1
 * @date: 2019/1/27 16:32
 */

package com.miaoshaproject.service.model;

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

	// 购买的商品id
	private Integer itemId;

	// 购买的数量
	private Integer amount;

	// 购买的金额
	private BigDecimal orderPrice;

	// 购买商品的单价
	private BigDecimal itemPrice;

}
