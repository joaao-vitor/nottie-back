package com.nottie.util;

import com.nottie.dto.response.SuccessResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static <T> ResponseEntity<SuccessResponseDTO<T>> buildSuccessResponse(T data, String message, HttpStatus status) {
        return new ResponseEntity<>(new SuccessResponseDTO<>(message, data, status.value()), status);
    }

}
