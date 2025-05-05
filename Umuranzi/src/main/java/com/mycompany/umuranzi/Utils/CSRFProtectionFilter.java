package com.mycompany.umuranzi.Utils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebFilter("/*")
public class CSRFProtectionFilter implements Filter {
    
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Generate token for GET requests
        if (req.getMethod().equalsIgnoreCase("GET")) {
            String token = UUID.randomUUID().toString();
            req.getSession().setAttribute("csrfToken", token);
            req.setAttribute("csrfToken", token);
        }

        // Validate token for POST/PUT/DELETE requests
        if (req.getMethod().equalsIgnoreCase("POST")) {
            String sessionToken = (String) req.getSession().getAttribute("csrfToken");
            String requestToken = req.getParameter("csrfToken");

            if (sessionToken == null || !sessionToken.equals(requestToken)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid CSRF Token");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
