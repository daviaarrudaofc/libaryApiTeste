package io.github.daviaarrudaofc.libaryAPI.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Libary API",
                version = "v1",
                contact = @Contact(
                        name = "Davi Arruda",
                        email = "daviarruda092@gmail.com",
                        url = "libaryapi.com"
                )
        )
)
public class OpenApiConfiguration {
}
