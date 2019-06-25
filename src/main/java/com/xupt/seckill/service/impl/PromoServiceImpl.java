/**
 * @author: maxu1
 * @date: 2019/1/27 19:52
 */

package com.xupt.seckill.service.impl;

import com.xupt.seckill.mapper.PromoMapper;
import com.xupt.seckill.pojo.Promo;
import com.xupt.seckill.service.PromoService;
import com.xupt.seckill.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author maxu
 */
@Service
public class PromoServiceImpl implements PromoService {
	@Resource
	private PromoMapper promoMapper;

	@Autowired
	private RedisTemplate redisTemplate;
	@Override
	public PromoModel getPromoByItemId(Integer itemId) {
		Promo promo = promoMapper.findByItemId(itemId);
		PromoModel promoModel = convertModelFromDataObject(promo);
		if (promoModel == null) {
			return null;
		}
		// 判断秒杀时间是
		DateTime now = new DateTime();
		if (promoModel.getStartDate().isAfterNow()) {
			promoModel.setStatus(1);
		} else if(promoModel.getEndDate().isBeforeNow()) {
			promoModel.setStatus(3);
		} else {
			promoModel.setStatus(2);
		}
		return promoModel;
	}

	@Override
	public void publishPromo(Integer promoId) {
		// TODO
	}

	@Override
	public String generateSecondKillToken(Integer promoId,Integer itemId,Integer userId) {
		Promo promo = promoMapper.findByItemId(promoId);
		PromoModel promoModel = convertModelFromDataObject(promo);
		if (promoModel == null) {
			return null;
		}

		// 判断用户的登陆信息


		// 判断秒杀时间是
		DateTime now = new DateTime();
		if (promoModel.getStartDate().isAfterNow()) {
			promoModel.setStatus(1);
		} else if(promoModel.getEndDate().isBeforeNow()) {
			promoModel.setStatus(3);
		} else {
			promoModel.setStatus(2);
		}
		if (promoModel.getStatus().intValue() != 2) {
			return null;
		}
		String token = UUID.randomUUID().toString().replace("-", "");
		redisTemplate.opsForValue().set("promo_token_" + promoId, token, 5, TimeUnit.MINUTES);
		return token;
	}

	private PromoModel convertModelFromDataObject(Promo promo) {
		if (promo == null) {
			return null;
		}
		PromoModel promoModel = new PromoModel();
		BeanUtils.copyProperties(promo,promoModel);
		// TODO
		return promoModel;
	}
}
