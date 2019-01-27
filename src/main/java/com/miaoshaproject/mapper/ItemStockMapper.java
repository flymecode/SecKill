package com.miaoshaproject.mapper;

import com.miaoshaproject.pojo.ItemStock;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author maxu
 */
@Mapper
public interface ItemStockMapper {

	void insertSeclective(ItemStock itemStock);

	ItemStock findByItemId(Integer id);
}
