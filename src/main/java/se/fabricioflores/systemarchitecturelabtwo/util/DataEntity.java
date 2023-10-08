package se.fabricioflores.systemarchitecturelabtwo.util;

import java.time.LocalDateTime;

public class DataEntity {
    public DataEntity(Object data, String info) {
        this.meta = new Object() {
            public final String message = info;
            public final LocalDateTime timestamp = LocalDateTime.now();
        };
        this.data = data;
    }

    public Object meta;
    public Object data;
}
