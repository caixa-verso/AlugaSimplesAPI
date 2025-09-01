package com.caixaverso.resource;

import com.caixaverso.model.auth.ApiKeys;
import com.caixaverso.model.auth.BCryptAdapter;
import com.caixaverso.model.auth.Usuario;
import io.quarkus.panache.common.Parameters;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Path("/api/v1/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @POST
    @Path("/sign-up")
    @Transactional
    public Response Cadastro(CriarUsuarioDTO dto) {

        String senhaCriptografada = BCryptAdapter.encrypt(dto.senha());

        Usuario usuario = new Usuario(dto.usuario(), senhaCriptografada, dto.email());

        usuario.persist();

        return Response.status(201).build();
    }


    @POST
    @Path("/sign-in")
    public Response Login(LoginDTO dto) {

        Optional<Usuario> possivelUsuario = Usuario.find("email = :email", Parameters.with("email", dto.email())).firstResultOptional();

        if (possivelUsuario.isEmpty()) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Usuario usuario = possivelUsuario.get();

        Boolean senhaIgual = BCryptAdapter.checkPassword(dto.senha, usuario.getSenha());

        if (senhaIgual) {
            String token = UUID.randomUUID().toString();

            ApiKeys.API_KEYS.add(token);

            return Response.ok(Map.of("token", token)).build();
        }

        return Response.status(Response.Status.FORBIDDEN).build();
    }


    public record CriarUsuarioDTO(String usuario, String senha, String email) {
    }

    public record LoginDTO(String email, String senha) {
    }

}
