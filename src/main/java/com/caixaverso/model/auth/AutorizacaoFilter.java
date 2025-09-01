package com.caixaverso.model.auth;

import io.quarkus.logging.Log;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AutorizacaoFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Request request = requestContext.getRequest();

        if (
                requestContext.getUriInfo().getRequestUri().getPath().endsWith("/api/v1/auth/sign-up") ||
                        requestContext.getUriInfo().getRequestUri().getPath().endsWith("/api/v1/auth/sign-in")
        ) {
            return;
        }
        Log.infof("Request recebida method: %s uri: %s", request.getMethod(), requestContext.getUriInfo().getRequestUri());

        String apiKey = requestContext.getHeaders().get("X-Api-key").get(0);

        if (!ApiKeys.API_KEYS.contains(apiKey)) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }

    }
}
