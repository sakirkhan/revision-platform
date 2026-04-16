package com.revision_platform.common.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        long startTime = System.currentTimeMillis();
        String remoteIp = getClientIp(request);
        
        // Skip logging for actuator or static resources if needed, but for now we log everything.
        log.info("API HIT START: [{} {}] - Client IP: {}", request.getMethod(), request.getRequestURI(), remoteIp);
        
        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            log.info("API HIT END:   [{} {}] - Status: {} - Time: {}ms", 
                request.getMethod(), request.getRequestURI(), response.getStatus(), duration);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        } else {
            // X-Forwarded-For can contain multiple IPs if there are multiple proxies. The first is the true client.
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }
}
