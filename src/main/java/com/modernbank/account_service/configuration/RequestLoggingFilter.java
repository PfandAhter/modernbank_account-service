package com.modernbank.account_service.configuration;

import com.modernbank.account_service.constants.HeaderKey;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.modernbank.account_service.constants.HeaderKey.USER_ID;

@Component
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        log.info("CorrelationID: {}, Request: {} {} - User: {}",
                request.getHeader(HeaderKey.CORRELATION_ID),
                request.getMethod(),
                request.getRequestURI(),
                request.getHeader(USER_ID));

        long startTime = System.currentTimeMillis();
        filterChain.doFilter(request, response);

        log.info("Response: {} - Duration: {}ms",
                response.getStatus(),
                System.currentTimeMillis() - startTime);
    }
}