package com.springboot.app.gateway.filters;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
//import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class EjemploGlobalFilter implements GlobalFilter, Ordered {

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		// TODO Auto-generated method stub
		//AQUÍ VA LA LOGICA DEL FILTRO PRE 
		logger.info("Ejecutando filtro pre");
		
		exchange.getRequest().mutate().headers(h -> h.add("token","123456"));
		
		return chain.filter(exchange).then(Mono.fromRunnable(()->{
			//AQUÍ VA LA LOGICA DEL FILTRO POST
			logger.info("Ejecutando filtro post");
			
			//-----SI VIENE UN TOKEN EN EL REQUEST, SE PASARÁ AL RESPONSE
			Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("token")).ifPresent(valor->{
				exchange.getResponse().getHeaders().add("token", valor);
			});
			//-----
			
			//SE CREA COOKIE "COLOR" CON VALOR "ROJO" Y SE PASA AL RESPONSE
			exchange.getResponse().getCookies().add("color", ResponseCookie.from("color","rojo").build());
			//AHORA SE MODIFICA EL CONTENIDO. SE MOSTRARÁ UN TEXTO PLANO EN VEZ DE UN JSON
			//exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
		}));
	}

	
	private final Logger logger = LoggerFactory.getLogger(EjemploGlobalFilter.class);


	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 1;
	} 
}
