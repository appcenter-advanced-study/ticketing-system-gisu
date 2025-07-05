package com.api_gateway.api_gateway.config;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("ticket-service", r -> r.path("/ticket/**")
                        .uri("http://ticket-service:8081"))
                .route("ticketStock-service", r -> r.path("/stock/**")
                        .uri("http://ticketStock-service:8082"))
                .route("reservation-service", r -> r.path("/reservation/**")
                        .uri("http://reservation-service:8083"))
                .build();
    }
}
