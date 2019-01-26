/**
 * @author: maxu1
 * @date: 2019/1/26 12:32
 */

package com.miaoshaproject.mapper;

import com.miaoshaproject.mapper.provider.UserInfoSQLProvider;
import com.miaoshaproject.pojo.UserInfo;
import org.apache.ibatis.annotations.*;

/**
 *
 * @author maxu
 */
@Mapper
public interface UserInfoMapper {

	@Select("SELECT * FROM user_info WHERE id = #{id}")
	UserInfo findUserById(@Param("id") Integer id);

	@SelectProvider(type = UserInfoSQLProvider.class, method = "insertSelective")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	void insertSelective(UserInfo userInfo);

	UserInfo findUserByTelphone(String telPhone);
}
