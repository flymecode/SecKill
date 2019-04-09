package com.xupt.seckill.service;


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
public class UserServiceImplTest {
	@Autowired
	private UserService userService;

	@Test
	public void getUser() {
		userService.getUser(1);
	}
}
