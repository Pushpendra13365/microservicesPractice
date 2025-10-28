package com.auth_service.config;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class ResponseConfig {

    public static ResponseEntity<Object> generate(HttpStatus status, String message, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", data);
        map.put("message", message);
        map.put("status", status.value());
        map.put("timestamp", new Timestamp(new Date().getTime()));
        return new ResponseEntity<>(map, status);
    }

    public static ResponseEntity<Object> generate(HttpStatus status, String message) {
        return generate(status, message, null);
    }

    public static ResponseEntity<Object> success(String message, Object data) {
        return generate(HttpStatus.OK, message, data);
    }

    public static ResponseEntity<Object> success(String message) {
        return generate(HttpStatus.OK, message, null);
    }

    public static ResponseEntity<Object> error(HttpStatus status, String message) {
        return generate(status, message, null);
    }
}
