/**
 * @author: maxu1
 * @date: 2019/1/27 13:35
 */

package com.xupt.seckill.controller;

import com.xupt.seckill.error.BusinessException;
import com.xupt.seckill.response.CommonReturnType;
import com.xupt.seckill.service.CacheService;
import com.xupt.seckill.service.ItemService;
import com.xupt.seckill.service.model.ItemModel;
import com.xupt.seckill.vo.ItemVO;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author maxu
 */
@Controller
@RequestMapping("/item")
@Slf4j
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private CacheService cacheService;

	@GetMapping("/list")
	@ResponseBody
	public CommonReturnType listItem(@RequestParam(name = "id") Integer id) {
		List<ItemModel> itemModels = itemService.listItem();
		List<ItemVO> itemVOS = itemModels.stream().map(itemModel -> {
			ItemVO itemVO = convertVOFromModel(itemModel);
			return itemVO;
		}).collect(Collectors.toList());

		return CommonReturnType.create(itemVOS);
	}

	@GetMapping("/{id}")
	@ResponseBody
	public CommonReturnType getItem(@PathVariable(name = "id") Integer id) {
		ItemModel itemModel = null;
		// 先获取本地缓存
		itemModel = (ItemModel) cacheService.getCommonCach("item_" + id);
		if (itemModel == null) {
			itemModel = (ItemModel)redisTemplate.opsForValue().get("item_" + id);
			if (itemModel == null) {
				itemModel = itemService.getItemById(id);
				redisTemplate.opsForValue().set("item_" + id, itemModel,30, TimeUnit.MINUTES);
			}
			// 填充本地缓存
			cacheService.setCommonCache("item_" + id, itemModel);
		}

		ItemVO itemVO = convertVOFromModel(itemModel);
		return CommonReturnType.create(itemVO);
	}

	@PostMapping("/create")
	@ResponseBody
	public CommonReturnType createItem(@RequestParam(name = "title") String title,
	                                   @RequestParam(name = "description") String description,
	                                   @RequestParam(name = "price") BigDecimal price,
	                                   @RequestParam(name = "stock") Integer stock,
	                                   @RequestParam(name = "imgUrl") String imgUrl) throws BusinessException {

		ItemModel itemModel = new ItemModel();
		itemModel.setTitle(title);
		itemModel.setDescription(description);
		itemModel.setPrice(price);
		itemModel.setStock(stock);
		itemModel.setImgUrl(imgUrl);

		ItemModel itemForReuturn = itemService.createItem(itemModel);
		ItemVO itemVO = convertVOFromModel(itemForReuturn);
		return CommonReturnType.create(itemVO);
	}

	private ItemVO convertVOFromModel(ItemModel itemModel) {
		if (itemModel == null) {
			return null;
		}
		ItemVO itemVO = new ItemVO();
		BeanUtils.copyProperties(itemModel, itemVO);
		if (itemModel.getPromoModel() != null) {
			// 有秒杀活动
			itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
			itemVO.setPromoId(itemModel.getPromoModel().getId());
			itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
			itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
		} else {
			itemVO.setPromoStatus(0);
		}
		return itemVO;
	}
}
