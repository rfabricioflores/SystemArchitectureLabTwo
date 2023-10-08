package se.fabricioflores.systemarchitecturelabtwo.util;

import java.time.LocalDateTime;

public class ErrorEntity {
    public ErrorEntity(String message, String uri) {
        this.meta = new Object() {
            public final String error = message;
            public final String path = uri;
            public final LocalDateTime timestamp = LocalDateTime.now();
        };
    }

    public Object meta;
}

