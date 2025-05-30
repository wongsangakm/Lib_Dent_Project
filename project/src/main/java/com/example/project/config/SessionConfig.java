package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 1800, cleanupCron = "0 */5 * * * *")
public class SessionConfig {

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("SESSIONID");
        serializer.setCookiePath("/");

        if (isLocalhost()) {
            serializer.setSameSite("Lax");
            serializer.setUseSecureCookie(false);
        } else {
            serializer.setSameSite("None");
            serializer.setUseSecureCookie(true);
        }

        return serializer;
    }

    private boolean isLocalhost() {
        return activeProfile.equals("") || activeProfile.equalsIgnoreCase("local") || activeProfile.contains("dev");
    }
}
