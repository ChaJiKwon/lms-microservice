package com.example.UserService.filter;


import jakarta.servlet.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.io.IOException;
import java.util.logging.Logger;

public class AuthorLogginAfterFilter implements Filter {
    private final Logger LOG= Logger.getLogger(AuthorLogginAfterFilter.class.getName());
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        if (auth!=null){
            LOG.info("User " + auth.getName() + " is successful logged in, with the role of " + auth.getAuthorities().toString());
        }
        chain.doFilter(request,response);
    }
}
