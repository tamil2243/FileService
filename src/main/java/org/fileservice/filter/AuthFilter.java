package org.fileservice.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthFilter implements Filter{
    

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        boolean loggedIn = false;

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("userId".equals(c.getName())) {
                    loggedIn = true;
                    break;
                }
            }
        }

        String uri = req.getRequestURI();

       
        if (uri.endsWith("signin") || uri.endsWith("signup") || uri.contains(".css") || uri.contains(".js") || uri.contains(".html")) {
            chain.doFilter(request, response);
            return;
        }

        if (!loggedIn) {
            
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        
        chain.doFilter(request, response);
    }
}
