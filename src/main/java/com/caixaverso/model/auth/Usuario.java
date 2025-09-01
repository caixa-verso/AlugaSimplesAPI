package com.caixaverso.model.auth;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class Usuario extends PanacheEntityBase {

    @Id
    private String id;
    private String usuario;
    private String senha;
    private String email;
    private Papeis papel;

    protected Usuario() {
    }

    public Usuario(String usuario, String senha, String email) {
        this.id = UUID.randomUUID().toString();
        this.usuario = usuario;
        this.senha = senha;
        this.email = email;
        this.papel = Papeis.USUARIO;
    }

    public String getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public Papeis getPapel() {
        return papel;
    }
}
