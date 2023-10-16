package se.fabricioflores.systemarchitecturelabtwo.util;

import java.time.LocalDateTime;

public class ErrorMeta {
    public ErrorMeta(String message, String uri) {
        this.error = message;
        this.path = uri;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorMeta() {}

    public String error;
    public String path;
    public LocalDateTime timestamp;
}
