package com.example.breakApp.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

// 클라이언트(프론트엔드)에서 백엔드 API를 호출할 수 있도록 허용
@Configuration
class WebConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://10.0.2.2:8089")  // 안드로이드 에뮬레이터의 백엔드 주소
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    }
}