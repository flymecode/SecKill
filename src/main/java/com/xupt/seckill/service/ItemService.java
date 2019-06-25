/**
 * @author: maxu1
 * @date: 2019/1/27 13:02
 */

package com.xupt.seckill.service;

import com.xupt.seckill.error.BusinessException;
import com.xupt.seckill.service.model.ItemModel;

import java.util.List;

/**
 *
 * @author maxu
 */
public interface ItemService {
	/**
	 * 创建商品
	 * @param itemModel
	 * @return
	 */
	ItemModel createItem(ItemModel itemModel) throws BusinessException;

	/**
	 * 商品浏览
	 * @return
	 */
	List<ItemModel> listItem();

	/**
	 * 商品详情
	 * @param id
	 * @return
	 */
	ItemModel getItemById(Integer id);


	/**
	 * 减少库存
	 * @param itemId
	 * @param amount
	 * @return
	 */
	Boolean decreaseStock(Integer itemId, Integer amount);

	/**
	 * 增加销量
	 * @param itemId
	 * @param amount
	 * @throws BusinessException
	 */
	void increaseSales(Integer itemId, Integer amount)throws BusinessException;

	/**
	 * item and promoModel cache model
	 * @param id
	 * @return
	 */
	ItemModel getItemByIdInCache(Integer id);
}
