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
                        .uri("lb://ticket-service"))
                .route("ticketStock-service", r -> r.path("/stock/**")
                        .uri("lb://ticketStock-service"))
                .route("reservation-service", r -> r.path("/reservation/**")
                        .uri("lb://reservation-service"))
                .build();
    }
}
