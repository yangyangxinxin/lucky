package com.luckysweetheart;

import com.luckysweetheart.service.UserService;
import com.luckysweetheart.utils.ResultInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LuckyWebApplication.class)
@WebAppConfiguration
public class LuckyWebApplicationTests {

	@Autowired
	private UserService userService;

	@Test
	public void contextLoads() {
	}

	@Test
	public void test1(){
		ResultInfo<Void> resultInfo = null;
		try {
			resultInfo = userService.login("yangxin","yangxin123");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(resultInfo);
	}

}
