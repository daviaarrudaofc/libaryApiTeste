package io.github.daviaarrudaofc.libaryAPI.security;

import io.github.daviaarrudaofc.libaryAPI.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustmoRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientService clientService;
    private final TokenSettings tokenSettings;
    private final ClientSettings clientSettings;

    @Override
    public void save(RegisteredClient registeredClient) {}// vai ter uma API propia pra isso, ent nao vai usar

    @Override
    public  RegisteredClient findById(String id) {
        return null;
    }

    @Override
    public  RegisteredClient findByClientId(String clientId) {

        var client = clientService.obterPorClientID(clientId);

        if(client == null){
            return null;
        }

        return RegisteredClient
                .withId(client.getId().toString())
                .clientId(client.getClientId())
                .clientSecret(client.getClientSecret())
                .redirectUri(client.getRedirectURI())
                .scope(client.getScope())
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC) // Envia client ID e secret pelo cabeçalho Basic para autenticar o cliente.
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) // Permite obter um token após o login do usuário e a troca do código.
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) // Permite ao cliente obter um token em nome próprio, sem login de usuário.
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN) // Permite trocar um refresh token válido por um novo access token.
                .tokenSettings(tokenSettings)
                .clientSettings(clientSettings)
                .build();
    }
}
