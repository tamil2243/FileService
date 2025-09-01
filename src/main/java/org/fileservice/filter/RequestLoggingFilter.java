package org.fileservice.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class RequestLoggingFilter implements Filter{

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("RequestLoggingFilter initialized.");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        long startTime = System.currentTimeMillis();

        System.out.println("Incoming Request URL: " + httpRequest.getRequestURL() + " | Method: " + httpRequest.getMethod());

        chain.doFilter(request, response);
	long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        System.out.println("Completed Request URL: " + httpRequest.getRequestURL() + " | Time Taken: " + duration + " ms");
    }

    @Override
    public void destroy() {
        System.out.println("RequestLoggingFilter destroyed.");
    }
    
}
