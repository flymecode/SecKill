/**
 * @author: maxu1
 * @date: 2019/1/26 12:52
 */

package com.xupt.seckill.pojo;

import lombok.Data;

/**
 *
 * @author maxu
 */
@Data
public class UserInfo {
	private Integer id;
	private String name;
	private Integer age;
	private Integer gender;
	private String telPhone;
	private String registerMode;
	private Integer thirdPartyId;
}
