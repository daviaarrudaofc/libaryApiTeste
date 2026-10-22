package io.github.daviaarrudaofc.libaryAPI.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.daviaarrudaofc.libaryAPI.model.Client;
import io.github.daviaarrudaofc.libaryAPI.security.CustomAuthentication;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

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


                //access_token: token utilizado nas requisições
                .accessTokenTimeToLive(Duration.ofMinutes(60))//config de duração do token
                //refresh_token: token para renovar o access_token
                .refreshTokenTimeToLive(Duration.ofMinutes(90))
                .build();
    }
    @Bean
    public ClientSettings clientSettings(){
        return ClientSettings.builder()
                .requireAuthorizationConsent(false)// n trabalha com janelinha de consentimento,que nao vai autorizar as info
                .build();
    }
    //JWK - Json Web Key
    // Disponibiliza ao Authorization Server a chave usada para assinar tokens.
    @Bean
    public JWKSource<SecurityContext> jwkSource() throws Exception{
        RSAKey rsaKey = gerarChaveRSA();

        JWKSet jwkSet = new JWKSet(rsaKey);// Inclui a chave gerada no conjunto
        return new ImmutableJWKSet<>(jwkSet);// Entrega esse conjunto ao Spring.
    }

    //Gerar par de chaves RSA
    // Cria um par: chave privada para assinar; pública para verificar a assinatura.
    private RSAKey gerarChaveRSA() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        RSAPublicKey chavePublica = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey chavePrivada = (RSAPrivateKey) keyPair.getPrivate();

        return new RSAKey
                .Builder(chavePublica)
                .privateKey(chavePrivada)
                .keyID(UUID.randomUUID().toString())// Identifica qual chave assinou o token
                .build()
                ;
    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource){
        // Usa as chaves disponíveis para decodificar e verificar JWTs recebidos.
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }


    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){
        return AuthorizationServerSettings.builder()
                //obter token
                .tokenEndpoint("/oauth2/token")
                // para consultar stautus do token
                .tokenIntrospectionEndpoint("/oauth2/introspect")
                //revogar
                .tokenRevocationEndpoint("/oauth2/revoke")
                //autorizar endpoint
                .authorizationEndpoint("/oauth2/authorize")
                //informações do usuario OPEN ID CONNECT
                .oidcUserInfoEndpoint("/oauth2/userinfo")
                //obter chave publica pra verificar a assinatura do token
                .jwkSetEndpoint("/oauth2/jwks")
                // logout
                .oidcLogoutEndpoint("/oauth2/logout")

                .build();
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer(){
        return context -> {
            var principal = context.getPrincipal();

            if(principal instanceof CustomAuthentication authentication){
                OAuth2TokenType tipoToken = context.getTokenType();

                if(OAuth2TokenType.ACCESS_TOKEN.equals(tipoToken)){

                    Collection<GrantedAuthority> authorities = authentication.getAuthorities();
                    List<String> authoritiesList =
                            authorities.stream().map(GrantedAuthority::getAuthority).toList();

                    context
                            .getClaims()
                            .claim("authorities", authoritiesList)
                            .claim("email", authentication.getUsuario().getEmail());

                }
            }
        };
    }
}
