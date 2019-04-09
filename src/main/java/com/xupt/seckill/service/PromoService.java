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

	PromoModel getPromoByItemId(Integer itemId);
}
