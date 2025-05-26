package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 1800)  // 30 นาที
public class SessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("SESSIONID");  // กำหนดชื่อ Cookie
        serializer.setCookiePath("/");
      
        serializer.setSameSite("None");        // เพื่อให้ cross-origin ได้
        serializer.setUseSecureCookie(true);  
        serializer.setDomainNamePattern("^(.+\\.)?vercel\\.app$");
        return serializer;
    }
}
