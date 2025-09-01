package com.caixaverso.model.auth;

import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class AutorizacaoFilter implements ContainerRequestFilter {

    private static final Set<String> ALLOWED_PATH = Set.of("/api/v1/auth/sign-up", "/api/v1/auth/sign-in");
    private static final String X_API_KEY = "X-Api-Key";
    private static final Map<String, Papeis> PATH_PAPEL = new ConcurrentHashMap<>();

    static {
        PATH_PAPEL.put("/api/v1/vehicles", Papeis.ADMIN);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {

        Request request = requestContext.getRequest();

        if (requestContext.getMethod().equals("POST") && isAllowedPath(requestContext)) {
            return;
        }

        List<String> xApiKey = requestContext.getHeaders().get(X_API_KEY);

        if (xApiKey == null || xApiKey.isEmpty()) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        } else {
            String apiKey = xApiKey.get(0);

            Papeis papelAssociadoAPIKey = ApiKeys.API_KEYS.get(apiKey);
            if (papelAssociadoAPIKey == null) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            String currentPath = requestContext.getUriInfo().getRequestUri().getPath();
            Papeis papelRequerido = PATH_PAPEL.get(currentPath);

            if (papelRequerido != null && !papelAssociadoAPIKey.equals(papelRequerido)) {

                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());

            }
        }
    }

    private static boolean isAllowedPath(ContainerRequestContext requestContext) {

        String currentPath = requestContext.getUriInfo().getRequestUri().getPath();

        return ALLOWED_PATH.stream().anyMatch(path -> path.equals(currentPath));
    }
}
