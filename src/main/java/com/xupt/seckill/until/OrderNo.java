/**
 * @author: maxu1
 * @date: 2019/1/27 17:27
 */

package com.xupt.seckill.until;

import com.xupt.seckill.mapper.SequenceMapper;
import com.xupt.seckill.pojo.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *  交易流水号
 * @author maxu
 */
@Component
public class OrderNo {

	@Autowired
	@Resource
	@NotNull
	private SequenceMapper sequenceMapper;

	// 订单号有16位
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public  String generateOrderNo () {
		StringBuilder stringBuilder = new StringBuilder();
		// 前8位为时间信息，年月日
		LocalDateTime now = LocalDateTime.now();
		String nowDate = now.format(DateTimeFormatter.ISO_DATE).replace("-", "");
		stringBuilder.append(nowDate);
		// 中间6位为自增序列
		int sequence = 0;

		Sequence order_info = sequenceMapper.getSequenceByName("order_info");
		// 最后2位为分库分表
		sequence = order_info.getCurrentValue();
		order_info.setCurrentValue(sequence + order_info.getStep());
		sequenceMapper.updateById(order_info);

		String str = String.valueOf(sequence);
		for (int i = 0; i < 6 - str.length();i++) {
			stringBuilder.append("0");
		}
		stringBuilder.append(sequence);
		stringBuilder.append("00");
		return stringBuilder.toString();
	}
}
