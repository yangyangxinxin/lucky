package com.luckysweetheart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/*@SpringBootApplication
@ServletComponentScan
public class LuckyWebApplication  extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(LuckyWebApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(LuckyWebApplication.class);
	}
}*/

@SpringBootApplication
@ServletComponentScan
public class LuckyWebApplication   {

	public static void main(String[] args) {
		SpringApplication.run(LuckyWebApplication.class, args);
	}

}
