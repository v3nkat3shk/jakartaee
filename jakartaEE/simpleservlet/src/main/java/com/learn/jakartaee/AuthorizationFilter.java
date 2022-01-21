package com.learn.jakartaee;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@WebFilter(filterName = "AuthorizationFilter", urlPatterns = "/1.security/*")
public class AuthorizationFilter implements Filter {
    private Map<String, String> passwords;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        passwords = new ConcurrentHashMap<>();
        passwords.put("admin", "admin");
        passwords.put("developer", "dev123");
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        if (!validateAuth((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse)) {
            ((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private boolean validateAuth(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getHeader("login");

        if (login == null || !passwords.containsKey(login))
            return false;

        String password = request.getHeader("password");
        if (password == null || !passwords.get(login).equals(password))
            return false;

        return true;
    }
}
