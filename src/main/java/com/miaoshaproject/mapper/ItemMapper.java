package com.miaoshaproject.mapper;

import com.miaoshaproject.pojo.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author maxu
 */
@Mapper
public interface ItemMapper {
	void insertSelective(Item item);

	Item findItemById(Integer id);

	List<Item> listItem();

	void increaseSales(@Param("itemId") Integer itemId,@Param("amount") Integer amount);
}
