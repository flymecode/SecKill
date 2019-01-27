package com.miaoshaproject.mapper;

import com.miaoshaproject.pojo.Item;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author maxu
 */
@Mapper
public interface ItemMapper {
	void insertSelective(Item item);

	Item findItemById(Integer id);
}
