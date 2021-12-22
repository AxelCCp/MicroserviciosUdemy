package com.springboot.app.item;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

	@Bean(name="clienteRest")
	@LoadBalanced  //CON ESTA SE HACE EL BALANCEO DE CARGA CON RIBBEN, MIENTRAS QUE  RESTTEMPLATE BUSCA LAS INSTANCIAS DISPONIBLES (CLASE 20)
	public RestTemplate registrarRestTemplate() {
		return new RestTemplate();
	}
}
