package com.example.apigateway.filter;
import com.example.apigateway.exception.ForbiddenUrlException;
import com.example.apigateway.service.RouterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Locale;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthorizationFilter implements GatewayFilter {
    private final RouterValidator routerValidator;
    private final WebClient.Builder webClientBuilder;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("authorization filter.....");
        ServerHttpRequest request = exchange.getRequest();
        List<String> userRoles = request.getHeaders().getOrEmpty("roles");

        // check if api is not public and need to authorize
        if (routerValidator.isSecured.test(request)) {
            //check role of user
            if (userRoles.isEmpty() || userRoles.get(0).isBlank()) {
                log.warn("No roles provided, rejecting request.");
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            String role = userRoles.get(0);
            String path = exchange.getRequest().getURI().getPath();
            String method = exchange.getRequest().getMethod().toString();
            log.info("Permission roles: {}, Path: {}, Method: {}", userRoles, path, method);
            // Gọi API user-service để kiểm tra quyền
            return webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8080/user/check-permission?path=" + path + "&method=" + method + "&role=" + role)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .filter(Boolean.TRUE::equals) // Chỉ tiếp tục nếu isAllowed == true
                    .switchIfEmpty(Mono.error(new ForbiddenUrlException("This url is forbidden, " + role.toUpperCase(Locale.ROOT) + " is not allowed to access")))
                    // Nếu isAllowed == true thì tiếp tục filter
                    .flatMap(isAllowed -> chain.filter(exchange));
        }
        return chain.filter(exchange);
    }
}
