/**
 * @author: maxu1
 * @date: 2019/1/27 19:49
 */

package com.xupt.seckill.pojo;

import lombok.Data;
import org.joda.time.DateTime;

import java.math.BigDecimal;

/**
 *
 * @author maxu
 */
@Data
public class Promo {

	private Integer id;

	// 秒杀活动名称
	private String promoName;

	// 秒杀活动的开始时间
	private DateTime startDate;

	// 秒杀活动的商品
	private Integer itemId;

	private BigDecimal promoItemPrice;
}
