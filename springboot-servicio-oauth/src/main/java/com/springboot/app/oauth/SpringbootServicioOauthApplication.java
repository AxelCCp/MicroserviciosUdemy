package com.springboot.app.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//CommandLineRunner: SE IMPLEMENTA PARA CREAR CONTRASEÑAS Y ENCRIPTARLAS. SE HACE ESTO PARA PROVAR EL FUNCIONAMIENTO DEL MICROSERVICIO OAUTH EN POSTMAN.

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class SpringbootServicioOauthApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootServicioOauthApplication.class, args);
	}
	
	//EN EL MÉTODO RUN() SE ENCRIPTAN LAS CONTRASEÑAS. PERO SE NECESITA EL BEAN BCryptPasswordEncoder QUE ESTÁ EN LA CLASE SpringSecurityConfig..
	@Override
	public void run(String... args) throws Exception {
		// SE CREA UNA CONTRASEÑA
		String password = "12345";
		//CON BCryptPasswordEncoder SE PUEDEN GENERAR VARIOS CÓDIGOS ENCRIPTADOS A PARTIR DE UNA CONTRASEÑA.
		for(int i=0; i<4 ; i++) {
			//SE GENERAN 4 CONTRASEÑAS
			String passwordBCrypt = passwordEncode.encode(password);
			System.out.println(passwordBCrypt);
		}
	}

	@Autowired
	private BCryptPasswordEncoder passwordEncode;
	
}
