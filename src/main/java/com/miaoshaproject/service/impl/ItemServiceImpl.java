/**
 * @author: maxu1
 * @date: 2019/1/27 13:04
 */

package com.miaoshaproject.service.impl;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.mapper.ItemMapper;
import com.miaoshaproject.mapper.ItemStockMapper;
import com.miaoshaproject.pojo.Item;
import com.miaoshaproject.pojo.ItemStock;
import com.miaoshaproject.service.ItemService;
import com.miaoshaproject.service.model.ItemModel;
import com.miaoshaproject.validator.ValidationResult;
import com.miaoshaproject.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author maxu
 */
@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	@Resource
	private ItemMapper itemMapper;

	@Autowired
	@Resource
	private ItemStockMapper itemStockMapper;

	@Autowired
	private ValidatorImpl validator;

	@Override
	@Transactional
	public ItemModel createItem(ItemModel itemModel) throws BusinessException {
		// 校验入参
		ValidationResult result = validator.validator(itemModel);
		if(result.isHasErrors()) {
			throw new BusinessException(EmBusinessError.PARAMETER_VALDITION_ERROR,result.getErrorsMsg());
		}
		// 转化itemModel --> POJO
		Item item = convertItemFromItemModel(itemModel);
		// 写入数据库
		itemMapper.insertSelective(item);
		itemModel.setId(item.getId());
		ItemStock itemStock = convertItemStockFromItemModel(itemModel);
		itemStockMapper.insertSeclective(itemStock);
		// 返回创建完成的对象
		return this.getItemById(itemModel.getId());
	}

	private ItemStock convertItemStockFromItemModel(ItemModel itemModel) {
		if (itemModel == null) {
			return null;
		}
		ItemStock itemStock = new ItemStock();
		itemStock.setItemId(itemModel.getId());
		itemStock.setStock(itemModel.getStock());
		return itemStock;
	}

	private Item convertItemFromItemModel(ItemModel itemModel) {
		if (itemModel == null) {
			return null;
		}
		Item item = new Item();
		BeanUtils.copyProperties(itemModel,item);
		item.setPrice(itemModel.getPrice().doubleValue());
		return item;
	}

	@Override
	public List<ItemModel> listItem() {
		return null;
	}

	@Override
	public ItemModel getItemById(Integer id) {
		Item item = itemMapper.findItemById(id);
		if (item == null) {
			return null;
		}
		// 操作获得库存数量
		ItemStock itemStock = itemStockMapper.findByItemId(id);
		// 将dataObject -> model
		ItemModel itemModel = convertModelFromDataObject(item, itemStock);
		return itemModel;
	}

	private ItemModel convertModelFromDataObject(Item item, ItemStock itemStock) {
		ItemModel itemModel = new ItemModel();
		BeanUtils.copyProperties(item, itemModel);
		itemModel.setPrice(new BigDecimal(item.getPrice()));
		itemModel.setSales(itemStock.getStock());
		return itemModel;
	}
}
