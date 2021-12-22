package com.springboot.app.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer //SE HABILITA COMO SERVIDOR DE CONFIGURACIÓN
@SpringBootApplication
public class SpringbootServicioConfigServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioConfigServerApplication.class, args);
	}

}
