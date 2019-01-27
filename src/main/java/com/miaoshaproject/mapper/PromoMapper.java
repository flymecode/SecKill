/**
 * @author: maxu1
 * @date: 2019/1/27 19:53
 */

package com.miaoshaproject.mapper;

import com.miaoshaproject.pojo.Promo;

/**
 *
 * @author maxu
 */
public interface PromoMapper {

	// 根据itemid获取即将进行的和正在进行的秒杀活动
	Promo findByItemId(Integer itemId);
}
