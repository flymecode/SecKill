/**
 * @author: maxu1
 * @date: 2019/1/27 17:49
 */

package com.xupt.seckill.mapper;

import com.xupt.seckill.pojo.Sequence;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

/**
 * @author maxu
 */
@Mapper
public interface SequenceMapper {

	@Results({
			@Result(property = "name", column = "name", javaType = String.class, jdbcType = JdbcType.VARCHAR),
			@Result(property = "currentValue", column = "current_value"),
			@Result(property = "step", column = "step")
	})
	@Select("select * from sequence_info where name = #{name} for update")
	Sequence getSequenceByName(@Param("name") String name);

	@Update("update sequence_info set current_value = #{currentValue} where name = #{name}")
	void updateById(Sequence sequence);
}
