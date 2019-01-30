/**
 * @author: maxu1
 * @date: 2019/1/26 19:50
 */

package com.miaoshaproject.mapper.provider;

import com.miaoshaproject.pojo.UserInfo;
import org.apache.ibatis.jdbc.SQL;

/**
 * @author maxu
 */
public class UserInfoSQLProvider {

	// InsertProvider
	// 自定义sql语句,支持 #{} 的格式
	public String insertSelective(UserInfo userInfo) {
		StringBuilder sql = new StringBuilder(new SQL() {{
			INSERT_INTO("user_info");
			VALUES("id", "id");
		}}.toString());

		return sql.toString();
	}
}
