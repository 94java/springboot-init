package cc.jiusi.springbootinit.config;

import cc.jiusi.springbootinit.interceptor.FreshInterceptor;
import cc.jiusi.springbootinit.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @blog: <a href="https://www.jiusi.cc">九思_Java之路</a>
 * @Author: 九思.
 * @CreateTime: 2024-05-01  11:49
 * @Description: MVC 配置类
 */
@Configuration
public class MVCConfig implements WebMvcConfigurer {
    @Resource
    private LoginInterceptor loginInterceptor;
    @Resource
    private FreshInterceptor freshInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 刷新 token 有效期
        registry.addInterceptor(freshInterceptor).order(0);
        // 权限校验
        registry.addInterceptor(loginInterceptor).order(1);

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**").addResourceLocations("/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/js/");
        registry.addResourceHandler("/pages/**").addResourceLocations("/pages/");
        registry.addResourceHandler("/plugins/**").addResourceLocations("/plugins/");
    }
}
