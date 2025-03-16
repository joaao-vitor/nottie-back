package com.nottie.exception;

import java.time.Instant;

public record DetailedExceptionResponse <T>(T data, String message, Instant timestamp, String details) {
}
