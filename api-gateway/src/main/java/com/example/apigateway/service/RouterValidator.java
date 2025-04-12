package com.example.apigateway.service;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;


/**
 * kiểm tra đường dẫn yêu cầu của một request, nếu đường dẫn đó không nằm trong danh sách các endpoint công khai
 * (openApiEndpoints) thì request sẽ được coi là yêu cầu bảo mật và phải đi kèm
 */
@Component
public class RouterValidator {
    public static final List<String> openEndpoints= List.of(
            "/auth/register",
            "/auth/login"
    );

    public Predicate<ServerHttpRequest> isSecured=
            request -> openEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().startsWith(uri));
}
