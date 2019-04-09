/**
 * @author: maxu1
 * @date: 2019/1/26 20:56
 */

package com.xupt.seckill.until;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author maxu
 */
public class EncodeByMd5 {
	public static String encode(String str) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		// 确定计算方法
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		BASE64Encoder base64Encoder = new BASE64Encoder();
		// 加密字符串
		String newstr = base64Encoder.encode(md5.digest(str.getBytes("utf-8")));
		return newstr;
	}
}
