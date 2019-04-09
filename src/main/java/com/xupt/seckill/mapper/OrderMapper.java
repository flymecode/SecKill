/**
 * @author: maxu1
 * @date: 2019/1/27 16:42
 */

package com.xupt.seckill.mapper;

import com.xupt.seckill.pojo.OrderInfo;

/**
 *
 * @author maxu
 */
public interface OrderMapper {

	void insertSelective(OrderInfo orderInfo);
}
