package com.xupt.seckill.mapper;

import com.xupt.seckill.pojo.ItemStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author maxu
 */
@Mapper
public interface ItemStockMapper {

	void insertSeclective(ItemStock itemStock);

	ItemStock findByItemId(Integer id);

	int decreaseStock(@Param("itemId") Integer itemId,@Param("amount") Integer amount);
}
