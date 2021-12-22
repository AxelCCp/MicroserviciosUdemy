package com.springboot.app.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
@EntityScan({"com.springboot.app.commons.usuarios.models.entity"})//CON ESTA VA A IR A ESCANEAR A ESTE LUGAR LAS CLASES EN EL MICROSERVICIO USUARIOS COMMONS. 
@SpringBootApplication
public class SpringbootServicioUsuarios8Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioUsuarios8Application.class, args);
	}

}
