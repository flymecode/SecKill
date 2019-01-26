/**
 * @author: maxu1
 * @date: 2019/1/26 13:20
 */

package com.miaoshaproject.service.model;

import lombok.Data;

/**
 *
 * @author maxu
 */
@Data
public class UserModel {
	private Integer id;
	private String name;
	private Integer gender;
	private String telPhone;
	private String registerMode;
	private Integer thirdPartyId;
	private String encrptPassword;
}
