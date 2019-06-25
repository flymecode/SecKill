package com.xupt.seckill.mapper;

import com.xupt.seckill.pojo.ItemStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author maxu
 */
@Mapper
public interface ItemStockMapper {

	void insertSeclective(ItemStock itemStock);

	ItemStock findByItemId(Integer id);

	// 这里数据库会有一个行锁
	@Update("update item_sock set stock = stock - #{amount} where item_id = #{item_id} and stock >= #{amount}")
	int decreaseStock(@Param("itemId") Integer itemId,@Param("amount") Integer amount);
}
