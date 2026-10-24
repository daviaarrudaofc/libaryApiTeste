package io.github.daviaarrudaofc.libaryAPI.config;

import io.github.daviaarrudaofc.libaryAPI.security.CustomUserDetailsService;
import io.github.daviaarrudaofc.libaryAPI.security.JwtCustomAuthenticationFilter;
import io.github.daviaarrudaofc.libaryAPI.security.LoginSocialSuccessHandler;
import io.github.daviaarrudaofc.libaryAPI.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true) // habilita que isso faça nos controllers
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            LoginSocialSuccessHandler successHandler,
            JwtCustomAuthenticationFilter jwtCustomAuthenticationFilter) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .formLogin(configurer ->{
                    configurer.loginPage("/login");
                })
                //.formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers("/login/**").permitAll();
                    authorize.requestMatchers(HttpMethod.POST, "/usuarios/**").permitAll();

                    authorize.anyRequest().authenticated();
                })
                .oauth2Login(oauth2 ->{
                    oauth2
                            .loginPage("/login")
                            .successHandler(successHandler);
                })
                .oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()))// vai usar JWT para
                                                                                                        //validar o usuario
                .addFilterAfter(jwtCustomAuthenticationFilter, BearerTokenAuthenticationFilter.class)
                .build();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().requestMatchers(// vai passar urls que ele deve ignorar no filtro do Security,nao vai nem
              "/v2/api-docs/**",                    //nao vai nem verifica,tipo se tem token ou se nao tem
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/webjars/**"

            );
    }


    // CONFIGURA O PREFIXO ROLE
    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults(){
        return  new GrantedAuthorityDefaults("");
    }

    // configura , no TOKEN JWT, o prefico SCOPE
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        var authoritiesConverter = new JwtGrantedAuthoritiesConverter();
        authoritiesConverter.setAuthorityPrefix("");

        var converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

        return converter;
    }
}
