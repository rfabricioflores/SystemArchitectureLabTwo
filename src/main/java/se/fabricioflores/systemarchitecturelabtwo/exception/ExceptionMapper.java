package se.fabricioflores.systemarchitecturelabtwo.exception;

import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.fabricioflores.systemarchitecturelabtwo.util.ErrorEntity;

@Provider
public class ExceptionMapper implements jakarta.ws.rs.ext.ExceptionMapper<Exception> {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionMapper.class);

    @Context
    UriInfo uriInfo;

    private String getPath() {
        String basePath = uriInfo.getBaseUri().getPath();
        String requestPath = uriInfo.getPath(false);
        return UriBuilder.fromPath(basePath).path(requestPath).build().toString();
    }

    @Override
    public Response toResponse(Exception exception) {
        logger.error(exception.getMessage());
        return Response.status(500).entity(new ErrorEntity("An error occurred, please try again!", getPath())).build();
    }
}
