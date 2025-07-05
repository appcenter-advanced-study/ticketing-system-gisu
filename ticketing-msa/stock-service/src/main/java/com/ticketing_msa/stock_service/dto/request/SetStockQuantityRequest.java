package com.ticketing_msa.stock_service.dto.request;


public record SetStockQuantityRequest(Long ticketId, Integer quantity) {
}
