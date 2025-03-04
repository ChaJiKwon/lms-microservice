package com.example.apigateway.config;
import com.example.apigateway.filter.AuthenticationFilter;
import com.example.apigateway.filter.AuthorizationFilter;
import com.example.apigateway.filter.SomeFilter;
import com.example.apigateway.service.RouterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.server.reactive.ServerHttpRequest;
import java.net.URI;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class GlobalFilterConfig {
    private final RouterValidator routerValidator;
    private final AuthenticationFilter authenticationFilter;
    private final AuthorizationFilter authorizationFilter;
    private final SomeFilter someFilter;

    @Bean
    public GlobalFilter authenticationGlobalFilter() {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            System.out.println("request: " + request);
            URI uri = request.getURI(); // Trả về một đối tượng URI
            System.out.println("Full URL: " + uri);
            // Nếu không phải là secured endpoint, bỏ qua AuthenticationFilter
            if (!routerValidator.isSecured.test(request)) {
                System.out.println("this request is not secured endpoint and public");
                return chain.filter(exchange); // Bỏ qua filter
            }
            // Áp dụng AuthenticationFilter cho các endpoint cần bảo mật
            System.out.println("request secured ");
            return authenticationFilter.filter(exchange, chain);
        };
    }

    @Bean
    public GlobalFilter authorizationGlobalFilter(){
        return (exchange, chain) ->{
            ServerHttpRequest request = exchange.getRequest();
            URI uri = request.getURI();
            if (!routerValidator.isSecured.test(request)) {
                log.info("URI {} is public", uri);
                return chain.filter(exchange); // Bỏ qua filter
            }
            return authorizationFilter.filter(exchange,chain);
        };
    }

    @Bean
    public GlobalFilter someGlobalFilter(){
        return someFilter::filter;
    }
}
