/**
 * @author: maxu1
 * @date: 2019/1/27 19:50
 */

package com.xupt.seckill.service;

import com.xupt.seckill.service.model.PromoModel;

/**
 *
 * @author maxu
 */
public interface PromoService {

	/**
	 * by item_id get promo
	 * @param itemId
	 * @return
	 */
	PromoModel getPromoByItemId(Integer itemId);

	/**
	 * publish promo
	 * @param promoId
	 */
	void publishPromo(Integer promoId);


	/**
	 * 生成秒杀令牌
	 * @param itemId
	 * @return
	 */
	String generateSecondKillToken(Integer promoId,Integer itemId,Integer userId);

}
