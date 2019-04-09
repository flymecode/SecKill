/**
 * @author: maxu1
 * @date: 2019/1/26 13:20
 */

package com.xupt.seckill.service.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * @author maxu
 */
@Data
public class UserModel {
	private Integer id;
	@NotBlank
	private String name;
	@NotNull(message = "不能不填")
	private Integer gender;
	@NotNull
	@Max(value = 150)
	@Min(value = 0)
	private Integer age;
	private String telPhone;
	private String registerMode;
	private Integer thirdPartyId;
	private String encrptPassword;
}
