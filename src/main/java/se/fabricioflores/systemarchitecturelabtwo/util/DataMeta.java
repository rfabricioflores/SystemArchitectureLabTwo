package se.fabricioflores.systemarchitecturelabtwo.util;

import java.time.LocalDateTime;

public class DataMeta {
    public DataMeta(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public DataMeta() {
    }

    public String message;
    public LocalDateTime timestamp;
}
