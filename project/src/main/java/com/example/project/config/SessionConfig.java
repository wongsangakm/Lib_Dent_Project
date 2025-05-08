package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800) // 30 นาที
public class SessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("SESSIONID");  // กำหนดชื่อ Cookie
        serializer.setCookiePath("/");
        serializer.setDomainName("localhost"); // หาก deploy ต้องเปลี่ยนเป็น domain จริง
        serializer.setUseSecureCookie(false); // เปลี่ยนเป็น true ถ้าใช้ HTTPS
        return serializer;
    }
}
