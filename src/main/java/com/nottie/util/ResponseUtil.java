package com.nottie.util;

import com.nottie.dto.response.SuccessResponseDTO;
import com.nottie.dto.response.SuccessResponseWithoutDataDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;

public class ResponseUtil {

    public static <T> ResponseEntity<SuccessResponseDTO<T>> buildSuccessResponse(T data, String message, HttpStatus status) {
        return new ResponseEntity<>(new SuccessResponseDTO<>(message, data, status.value()), status);
    }
    public static ResponseEntity<SuccessResponseWithoutDataDTO> buildSuccessResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(new SuccessResponseWithoutDataDTO(message, status.value(), Instant.now()), status);
    }
}
