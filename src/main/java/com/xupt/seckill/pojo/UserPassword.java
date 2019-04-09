/**
 * @author: maxu1
 * @date: 2019/1/26 13:27
 */

package com.xupt.seckill.pojo;

import lombok.Data;

/**
 *
 * @author maxu
 */
@Data
public class UserPassword {
	private Integer id;
	private Integer userId;
	private String encrptPassword;
}
