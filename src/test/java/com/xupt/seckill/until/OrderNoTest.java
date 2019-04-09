package com.xupt.seckill.until;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author maxu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderNoTest {

	@Autowired
	private OrderNo orderNo;

	@Test
	public void generateOrderNo() {
		System.out.println(orderNo.generateOrderNo());
	}
}