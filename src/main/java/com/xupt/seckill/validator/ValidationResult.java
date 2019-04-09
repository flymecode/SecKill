/**
 * @author: maxu1
 * @date: 2019/1/27 11:43
 */

package com.xupt.seckill.validator;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 结果校验
 *
 * @author maxu
 */
@Data
public class ValidationResult {

	private boolean hasErrors = false;

	private Map<String, String> errorsMsgMap = new HashMap<>();

	public String getErrorsMsg() {
		return StringUtils.join(errorsMsgMap.values().toArray(),",");
	}

}
