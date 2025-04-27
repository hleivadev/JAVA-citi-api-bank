package com.citi.financialtransactionstaxes.apiciti.domain.response;

import java.time.LocalDateTime;

public class GenericResponse<T> {
    
    private final LocalDateTime timestamp;
    private final String message;
    private final T data;
    
    public GenericResponse(String message, T data) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.data = data;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
