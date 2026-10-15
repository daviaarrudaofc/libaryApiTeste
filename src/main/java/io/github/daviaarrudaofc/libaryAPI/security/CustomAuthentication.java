package io.github.daviaarrudaofc.libaryAPI.security;

import io.github.daviaarrudaofc.libaryAPI.model.Usuario;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class CustomAuthentication implements Authentication {

    private final Usuario usuario;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.usuario
                .getRoles() //POIS COMO NOS ESTAMOS CUSTOMIZANDO DO NOSSO JEITO,o Spring tem um prefixo ja pre definido como ROLES_ ...
                .stream()
                //.map(role -> new SimpleGrantedAuthority(role)) //new SimpleGrantedAuthority("ROLE_" + role))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable Object getCredentials() {
        return null;
    }

    @Override
    public @Nullable Object getDetails() {
        return usuario;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return usuario;
    }

    @Override
    public boolean isAuthenticated() {
        return true; // se deixar falso aq,nunca vai conseguir logar
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return usuario.getLogin();
    }
}
