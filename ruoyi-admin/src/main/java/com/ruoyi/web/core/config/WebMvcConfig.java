//package com.ruoyi.web.core.config;
//
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import springfox.documentation.spring.web.SpringfoxWebMvcConfiguration;
//
///**
// * @author dss
// * @version 1.0.0
// * @description TODO
// * @className WebMvcConfig.java
// * @createTime 2020年12月21日 14:11:00
// */
//@SpringBootApplication
//@ConditionalOnClass(SpringfoxWebMvcConfiguration.class)
//public class WebMvcConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
//}
