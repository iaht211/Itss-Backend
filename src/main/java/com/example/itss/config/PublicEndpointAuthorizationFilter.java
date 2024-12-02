package com.example.itss.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class PublicEndpointAuthorizationFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_ENDPOINTS = Arrays.asList(
            "/api/v1/auth/login",
            "/api/v1/auth/refresh",
            "/api/v1/users/register",
            "/api/v1/public/*"
    // Thêm các endpoint khác vào đây
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();
        if (isPublicEndpoint(uri)) {
            request = new HttpServletRequestWrapper(request) {
                @Override
                public String getHeader(String name) {
                    if ("Authorization".equalsIgnoreCase(name)) {
                        return null;
                    }
                    return super.getHeader(name);
                }
            };
        }
        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String uri) {
        return PUBLIC_ENDPOINTS.stream().anyMatch(uri::startsWith);
    }
}
