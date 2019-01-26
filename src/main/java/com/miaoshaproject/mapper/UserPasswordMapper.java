/**
 * @author: maxu1
 * @date: 2019/1/26 13:35
 */

package com.miaoshaproject.mapper;

import com.miaoshaproject.pojo.UserPassword;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author maxu
 */
@Mapper
public interface UserPasswordMapper {

	@Select("SELECT * from user_password where user_id = #{id}")
	UserPassword findUserPasswordByUserId(@Param("id") Integer id);
}
