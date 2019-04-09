/**
 * @author: maxu1
 * @date: 2019/1/27 12:49
 */

package com.xupt.seckill.service.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 *
 * @author maxu
 */
@Data
public class ItemModel {
	private Integer id;
	@NotNull(message = "商品价格不能为空")
	private String title;
	private BigDecimal price;
	private Integer stock;
	private String description;
	private Integer sales;
	private String imgUrl;

	//使用聚合模型，如果promoModel不为空，则表示还拥有还没结束的秒杀活动
	private PromoModel promoModel;
}
