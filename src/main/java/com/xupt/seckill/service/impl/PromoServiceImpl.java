/**
 * @author: maxu1
 * @date: 2019/1/27 19:52
 */

package com.xupt.seckill.service.impl;

import com.xupt.seckill.mapper.PromoMapper;
import com.xupt.seckill.pojo.Promo;
import com.xupt.seckill.service.ItemService;
import com.xupt.seckill.service.PromoService;
import com.xupt.seckill.service.model.ItemModel;
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
	private ItemService itemService;

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
		// 通过活动id 获取活动
		Promo promo = promoMapper.findById(promoId);
		if (promo.getItemId() == null || promo.getItemId().intValue() == 0) {
			return;
		}
		// TODO 如果存入内存，有可能你读取的时候，此时数据的的商品已经被修改了。我们这里可以设置为活动开始的时候将库存信息存入数据库？
		// 我们可以在业务层面必须下架的状态才可以
		ItemModel itemModel = itemService.getItemById(promo.getItemId());
		// 将库存存入内存
		redisTemplate.opsForValue().set("promo_item_stock_" + itemModel.getId(), itemModel.getStock());

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
