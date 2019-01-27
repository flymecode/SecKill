/**
 * @author: maxu1
 * @date: 2019/1/27 13:35
 */

package com.miaoshaproject.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @author maxu
 */
@Data
public class ItemVO {
	private Integer id;
	private String title;
	private BigDecimal price;
	private Integer stock;
	private String description;
	private Integer sales;

	private String imgUrl;
	// 商品是否在秒杀活动，0 没有秒杀活动，1，2，3
	private Integer promoStatus;

	private BigDecimal promoPrice;

	// 秒杀活动Id
	private Integer promoId;

	// 开始时间
	private String startDate;

}
