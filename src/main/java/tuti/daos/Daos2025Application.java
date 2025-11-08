package tuti.daos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

//probar ingresando a http://localhost:8080/swagger-ui/index.html#/
@OpenAPIDefinition(info = @Info(
contact = @Contact( 
name ="Dardo",
email="contactenos@email.com",
url = "www.mipaginaweb.com"),
description = "Aplicacion de Ejemplo DAO",
title = "DAOS API",
version = "1.0",
license = @License(name = "Apache 2.0", url = "www.miapirest.licence.com"),
termsOfService = "Terminos y condiciones"
)
)
@SpringBootApplication
public class Daos2025Application {

	public static void main(String[] args) {
		SpringApplication.run(Daos2025Application.class, args);
	}

}
