package com.luckysweetheart.config.velocity;

import org.springframework.boot.autoconfigure.velocity.VelocityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

/**
 * Created by yangxin on 2017/5/22.
 */
@Configuration
public class VelocityConfig {

    @Bean(name = "velocityViewResolver")
    public VelocityViewResolver velocityViewResolver(VelocityProperties properties) {
        VelocityViewResolver viewResolver = new VelocityViewResolver();
        viewResolver.setViewClass(VelocityLayoutToolboxView.class);
        properties.applyToViewResolver(viewResolver);// 设置默认属性，比如前缀和后缀
        return viewResolver;
    }

}
