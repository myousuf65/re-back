package com.hkmci.csdkms.exception;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class CustomExceptionSchema {
    /**
     *
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    protected CustomExceptionSchema() {}
  
    public CustomExceptionSchema(
//        LocalDateTime timestamp, int status, String error, String message, String path) {
            String message) {
//        this.timestamp = timestamp;
//        this.status = status;
//        this.error = error;
        this.message = message;
//        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}