// SessionUserFilter.java
package com.example.project.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Collections;
@Component
public class SessionUserFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);

        if (session != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            Long userId = (Long) session.getAttribute("userId");
            String username = (String) session.getAttribute("username");

            System.out.println("SessionUserFilter: userId=" + userId + ", username=" + username);

            if (userId != null && username != null) {
                var auth = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(request, response);
    }
}
