package com.ordermanagement.orderline;

public record OrderLineCreateRequest(
        String productSkuCode,
        Integer quantity,
        Long orderId
) {
}
