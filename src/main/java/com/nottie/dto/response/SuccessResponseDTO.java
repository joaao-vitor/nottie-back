package com.nottie.dto.response;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

public class SuccessResponseDTO <T>{
    private String message;
    private T data;
    private Instant timestamp;
    private int statusCode;

    public SuccessResponseDTO() {}
    public SuccessResponseDTO(String message, T data, int statusCode) {
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
        this.timestamp = Instant.now();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuccessResponseDTO<?> that = (SuccessResponseDTO<?>) o;
        return statusCode == that.statusCode && Objects.equals(message, that.message) && Objects.equals(data, that.data) && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, data, timestamp, statusCode);
    }

    @Override
    public String toString() {
        return "SuccessResponseDTO{" +
                "message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                ", statusCode=" + statusCode +
                '}';
    }
}
