/**
 * @author: maxu1
 * @date: 2019/1/27 16:42
 */

package com.miaoshaproject.mapper;

import com.miaoshaproject.pojo.OrderInfo;

/**
 *
 * @author maxu
 */
public interface OrderMapper {

	void insertSelective(OrderInfo orderInfo);
}
