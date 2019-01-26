/**
 * @author: maxu1
 * @date: 2019/1/26 19:50
 */

package com.miaoshaproject.mapper.provider;

import com.miaoshaproject.pojo.UserInfo;
import org.apache.ibatis.jdbc.SQL;

/**
 *
 * @author maxu
 */
public class UserInfoSQLProvider {

	public String insertSelective(UserInfo userInfo) {
		return new StringBuffer(new SQL(){{
			INSERT_INTO("user_info");
			VALUES("id","id");
		}}.toString()).toString();

	}
}
