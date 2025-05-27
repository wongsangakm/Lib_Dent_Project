package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

@Configuration
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 1800, cleanupCron = "0 */5 * * * *"    )  // 30 นาที
public class SessionConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("SESSIONID");  // กำหนดชื่อ Cookie
        serializer.setCookiePath("/");
      
          // 🔥 CRITICAL FIX for iOS Safari
        serializer.setSameSite("Lax");  // เปลี่ยนจาก "None" เป็น "Lax"
        serializer.setUseSecureCookie(true);  // ยังคงใช้ HTTPS
        serializer.setUseSecureCookie(true);  
        
        return serializer;
    }
}
