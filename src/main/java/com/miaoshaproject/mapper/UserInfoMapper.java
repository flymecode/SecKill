/**
 * @author: maxu1
 * @date: 2019/1/26 12:32
 */

package com.miaoshaproject.mapper;

import com.miaoshaproject.pojo.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author maxu
 */
@Mapper
public interface UserInfoMapper {

	@Select("SELECT * FROM user_info WHERE id = #{id}")
	UserInfo findUserById(@Param("id") Integer id);
}
