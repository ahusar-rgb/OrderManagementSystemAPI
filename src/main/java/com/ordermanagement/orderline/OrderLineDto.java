package com.ordermanagement.orderline;

public record OrderLineDto (
        Long id,
        String productSkuCode,
        Integer quantity
) {
}
