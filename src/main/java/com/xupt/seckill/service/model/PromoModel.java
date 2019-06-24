/**
 * @author: maxu1
 * @date: 2019/1/27 19:37
 */

package com.xupt.seckill.service.model;

import lombok.Data;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author maxu
 */
@Data
public class PromoModel implements Serializable {
	private Integer id;

	private Integer status;
	// 秒杀活动名称
	private String promoName;

	// 秒杀活动的开始时间

	private DateTime startDate;

	private DateTime endDate;

	// 秒杀活动的商品
	private Integer itemId;



	private BigDecimal promoItemPrice;
}
