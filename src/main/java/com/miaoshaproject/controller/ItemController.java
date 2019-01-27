/**
 * @author: maxu1
 * @date: 2019/1/27 13:35
 */

package com.miaoshaproject.controller;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.vo.ItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
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
		ItemModel itemModel = itemService.getItemById(id);
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
			itemVO.setStartDate(itemModel.getPromoModel().getStartDate());
		} else {
			itemVO.setPromoStatus(0);
		}
		return itemVO;
	}
}
