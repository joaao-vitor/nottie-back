package com.nottie.dto.response;

import java.time.Instant;

public record SuccessResponseWithoutDataDTO (String message, int statusCode, Instant timestamp) {
}
