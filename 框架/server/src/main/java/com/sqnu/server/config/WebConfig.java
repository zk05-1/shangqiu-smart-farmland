package com.sqnu.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置
 * <p>
 * 配置静态资源映射，使得上传的文件可以通过URL访问。
 * </p>
 *
 * @author sqnu
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 文件上传路径
     */
    @Value("${file.upload.path:D:/uploads/}")
    private String uploadPath;

    /**
     * 配置全局跨域（CORS）
     * <p>
     * 部署后前端(Vercel域名)与后端(Koyeb域名)不同源，
     * 必须放行跨域请求，否则浏览器会拦截 API 调用。
     * </p>
     *
     * @param registry 跨域注册器
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 配置静态资源映射
     * <p>
     * 将 /uploads/** 的请求映射到本地的 D:/uploads/ 目录，
     * 从而实现文件访问功能。
     * </p>
     *
     * @param registry 资源处理器注册表
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置上传文件的静态资源映射
        // 请求路径: /uploads/**
        // 实际路径: D:/uploads/**
        String resourcePath = "file:" + uploadPath;

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourcePath);
    }
}