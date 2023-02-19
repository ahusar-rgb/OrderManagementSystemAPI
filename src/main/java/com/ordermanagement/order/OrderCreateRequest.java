package com.ordermanagement.order;

import com.ordermanagement.orderline.OrderLineCreateRequest;

import java.time.LocalDate;
import java.util.List;

public record OrderCreateRequest(
        Long id,
        Long customerCode,
        LocalDate dateOfSubmission,
        List<OrderLineCreateRequest> orderLines)
{ }
