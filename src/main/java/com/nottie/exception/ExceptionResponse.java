package com.nottie.exception;

import java.util.Date;

public record ExceptionResponse(String message, Date timestamp, String details) {
}
