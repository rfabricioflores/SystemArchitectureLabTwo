package se.fabricioflores.systemarchitecturelabtwo.util;

public class ErrorEntity {
    public ErrorMeta meta;

    public ErrorEntity(String message, String uri) {
        this.meta = new ErrorMeta(message, uri);
    }

    public ErrorEntity() {}
}