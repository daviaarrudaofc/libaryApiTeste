package io.github.daviaarrudaofc.libaryAPI.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.daviaarrudaofc.libaryAPI.model.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
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
                .accessTokenTimeToLive(Duration.ofMinutes(60))//config de duração do token
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
}
