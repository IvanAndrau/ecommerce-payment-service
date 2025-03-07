package com.example.ecommerce_payment_service.Configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:8080"); //
        server.setDescription("Swagger for handy development");

        Contact myContact = new Contact();
        myContact.setName("Ivan");
        myContact.setEmail("ivan.andrau@gmail.com");

        Info information = new Info()
                .title("Payment Service API")
                .version("1.0")
                .description("This API exposes endpoints to manage payment-related transactions.")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}