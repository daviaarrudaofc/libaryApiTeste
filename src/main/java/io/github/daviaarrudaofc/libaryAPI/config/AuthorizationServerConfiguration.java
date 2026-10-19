package io.github.daviaarrudaofc.libaryAPI.config;

import io.github.daviaarrudaofc.libaryAPI.model.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.authorization.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.time.Duration;

@Configuration
@EnableWebSecurity
public class AuthorizationServerConfiguration {

    @Bean
    @Order(1)// Esta cadeia é analisada antes da cadeia geral.
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        http.oauth2AuthorizationServer(authorizationServer -> { // Configura o Authorization Server.
            http.securityMatcher(authorizationServer.getEndpointsMatcher()); // Aplica esta cadeia só aos endpoints dele.
            authorizationServer.oidc(Customizer.withDefaults()); // Habilita os recursos de identidade do OIDC.
        });

        http.authorizeHttpRequests(authorize -> // Define as regras de acesso desta cadeia.
                authorize.anyRequest().authenticated()); // Exige autenticação nas requisições que chegaram a ela.

        http.oauth2ResourceServer(resourceServer -> // Configura o recebimento de tokens nesta cadeia.
                resourceServer.jwt(Customizer.withDefaults())); // Valida os JWTs recebidos.

        http.formLogin(form -> form.loginPage("/login")); // Indica a página de login quando ela for necessária.

        return http.build(); // Monta e devolve esta cadeia de segurança.
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }


    @Bean
    public TokenSettings tokenSettings(){
        return TokenSettings.builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(Duration.ofMinutes(60))//config de duração do token
                .build();
    }
    @Bean
    public ClientSettings clientSettings(){
        return ClientSettings.builder()
                .requireAuthorizationConsent(false)// n trabalha com janelinha de consentimento,que nao vai autorizar as info
                .build();
    }
}
