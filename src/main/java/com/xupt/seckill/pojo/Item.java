/**
 * @author: maxu1
 * @date: 2019/1/27 12:57
 */

package com.xupt.seckill.pojo;

import lombok.Data;

/**
 *
 * @author maxu
 */
@Data
public class Item {
	private Integer id;
	private String title;
	private Double price;
	private Integer stock;
	private String description;
	private Integer sales;
	private String imgUrl;
}
