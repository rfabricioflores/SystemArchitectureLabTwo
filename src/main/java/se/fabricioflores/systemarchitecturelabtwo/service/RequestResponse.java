package se.fabricioflores.systemarchitecturelabtwo.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response;

// Must give this a better name
@RequestScoped
public class RequestResponse {
    private final ResponseBuilder res;

    public RequestResponse() {
        this.res = Response.notModified();
    }

    public ResponseBuilder getRes() {
        return res;
    }
}
