/**
 * @author: maxu1
 * @date: 2019/1/27 19:52
 */

package com.miaoshaproject.service.impl;

import com.miaoshaproject.mapper.PromoMapper;
import com.miaoshaproject.pojo.Promo;
import com.miaoshaproject.service.PromoService;
import com.miaoshaproject.service.model.PromoModel;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * @author maxu
 */
@Service
public class PromoServiceImpl implements PromoService {
	@Autowired
	@Resource
	private PromoMapper promoMapper;
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
