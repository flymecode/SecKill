/**
 * @author: maxu1
 * @date: 2019/1/26 13:35
 */

package com.xupt.seckill.mapper;

import com.xupt.seckill.mapper.provider.UserInfoSQLProvider;
import com.xupt.seckill.pojo.UserPassword;
import org.apache.ibatis.annotations.*;

/**
 *
 * @author maxu
 */
@Mapper
public interface UserPasswordMapper {

	@Select("SELECT * from user_password where user_id = #{id}")
	UserPassword findUserPasswordByUserId(@Param("id") Integer id);

	@SelectProvider(type = UserInfoSQLProvider.class, method = "insertSelective")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insertSelective(UserPassword userPassword);
}
