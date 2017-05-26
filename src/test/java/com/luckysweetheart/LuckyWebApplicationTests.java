package com.luckysweetheart;

import com.luckysweetheart.dal.entity.Article;
import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.service.ArticleService;
import com.luckysweetheart.service.UserService;
import com.luckysweetheart.store.StoreService;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.vo.StoreDataDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LuckyWebApplication.class)
@WebAppConfiguration
public class LuckyWebApplicationTests {

	@Autowired
	private UserService userService;

	@Test
	public void contextLoads() {
	}

	@Resource
	private StoreService storeService;

	@Resource
	private ArticleService articleService;

	@Test
	public void test1(){
		ResultInfo<UserDTO> resultInfo = null;
		try {
			resultInfo = userService.login("yangxin","yangxin123");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(resultInfo);
	}

	@Test
	public void test2(){
		ResultInfo<StoreDataDTO> resultInfo =
				storeService.uploadFile("F:\\code\\\\my\\\\lucky\\\\src\\\\main\\\\resources\\\\static\\\\img\\\\defaultUserImg.png","defaultUserImg.png");

		System.out.println(resultInfo);
	}

	@Test
	public void test3(){

	}

	@Test
	public void test4(){
		ResultInfo<Void> resultInfo = storeService.deleteFile("/1253770331/bubu/defaultUserImg.png");
		System.out.println(resultInfo);
	}

	@Test
	public void test5(){
		String result = storeService.download("/apply_02.jpg","D://yangxinxin.jpg");
		System.out.println(result);
	}

	@Test
	public void test6(){
		try {
			byte[] bytes = storeService.download("/apply_02.jpg");
			System.out.println(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
