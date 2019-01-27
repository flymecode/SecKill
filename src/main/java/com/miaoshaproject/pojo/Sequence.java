/**
 * @author: maxu1
 * @date: 2019/1/27 17:46
 */

package com.miaoshaproject.pojo;

import lombok.Data;

/**
 *
 * @author maxu
 */
@Data
public class Sequence {

	private String name;
	private Integer currentValue;
	private Integer step;
}
