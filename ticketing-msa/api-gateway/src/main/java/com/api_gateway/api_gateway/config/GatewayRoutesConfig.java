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

                //  Swagger API Docs 경로
                .route("ticket-docs", r -> r.path("/v3/api-docs/ticket")
                        .filters(f -> f.setPath("/v3/api-docs"))  // 내부 경로로 치환
                        .uri("lb://ticket-service"))

                .route("stock-docs", r -> r.path("/v3/api-docs/stock")
                        .filters(f -> f.setPath("/v3/api-docs"))
                        .uri("lb://stock-service"))

                .route("reservation-docs", r -> r.path("/v3/api-docs/reservation")
                        .filters(f -> f.setPath("/v3/api-docs"))
                        .uri("lb://reservation-service"))

                //  API 경로 라우팅
                .route("ticket-service", r -> r.path("/api/v1/tickets/**")
                        .uri("lb://ticket-service"))

                .route("stock-service", r -> r.path("/api/v1/stocks/**")
                        .uri("lb://stock-service"))

                .route("reservation-service", r -> r.path("/api/v1/reservations/**")
                        .uri("lb://reservation-service"))

                .build();
    }
}
//

