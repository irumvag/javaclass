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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@WebFilter("/api/*")
public class RateLimitFilter implements Filter {
    
    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final int MAX_REQUESTS = 100; // Per minute

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        String ip = ((HttpServletRequest)request).getRemoteAddr();
        AtomicInteger count = requestCounts.computeIfAbsent(ip, k -> new AtomicInteger(0));
        
        if(count.incrementAndGet() > MAX_REQUESTS) {
            ((HttpServletResponse)response).sendError(429, "Too Many Requests");
            return;
        }
        
        chain.doFilter(request, response);
        
        // Reset counter every minute
        new Timer().schedule(new TimerTask() {
            public void run() {
                requestCounts.remove(ip);
            }
        }, 60 * 1000);
    }
}
