package com.ordermanagement.order;

import com.ordermanagement.orderline.OrderLineDto;

import java.time.LocalDate;
import java.util.List;

public record OrderDto (
    Long id,
    Long customerCode,
    LocalDate dateOfSubmission,
    List<OrderLineDto> orderLines
) {

}
