package com.example.apigateway.exception;
import com.example.apigateway.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;
import io.jsonwebtoken.security.SignatureException;

import java.util.Arrays;


@Component
@Slf4j
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  WebProperties webProperties,
                                  ApplicationContext applicationContext,
                                  ServerCodecConfigurer configurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(configurer.getWriters());
        this.setMessageReaders(configurer.getReaders());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(),this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request){
        Throwable error = this.getError(request);
        log.info("Error in rq: {}", String.valueOf(error));
        ErrorResponse response = new ErrorResponse();
        if (error instanceof ExpiredJwtException
                || error instanceof MalformedJwtException
                || error instanceof SignatureException){
            response.setPath(request.path());
            response.setStatusCode(401);
            response.setMessage("Invalid token please try again");
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                    .body(BodyInserters.fromValue(response));
        }
        if (error instanceof LoginRequiredException loginRequiredException){
            response.setPath(request.path());
            response.setStatusCode(401);
            response.setMessage(loginRequiredException.getMessage());
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                    .body(BodyInserters.fromValue(response));
        }
        log.info("Error in rq: {}", String.valueOf(error));
        response.setMessage("Unexpected error");
        response.setPath(request.path());
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(BodyInserters.fromValue(response));
    }
}
