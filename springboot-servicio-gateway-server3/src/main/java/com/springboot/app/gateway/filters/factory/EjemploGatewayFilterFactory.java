package com.springboot.app.gateway.filters.factory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class EjemploGatewayFilterFactory extends AbstractGatewayFilterFactory<EjemploGatewayFilterFactory.Configuracion> {

	public EjemploGatewayFilterFactory() {
		super(Configuracion.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public GatewayFilter apply(Configuracion config) {
		// CON UNA EXPRESION LAMBDA SE IMPLEMENTA EL MÉTODO FILTER QUE TIENE LOS ARGUMENTOS (exchange, chain)
		return (exchange, chain) -> {
			//CÓDIGO FILTRO PRE
			logger.info("EJECUTANDO PRE GATEWAY FILTER FACTORY: " + config.mensaje);
			return chain.filter(exchange).then(Mono.fromRunnable(()->{
				//CÓDIGO FILTRO POST
				Optional.ofNullable(config.cookieValor).ifPresent(cookie -> {
					exchange.getResponse().addCookie(ResponseCookie.from(config.cookieNombre, cookie).build());
				});
				logger.info("EJECUTANDO POST GATEWAY FILTER FACTORY: " + config.mensaje);
			}));	
			
		};
	}
	
	public static class Configuracion {
		
		public String getMensaje() {
			return mensaje;
		}
		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		public String getCookieValor() {
			return cookieValor;
		}
		public void setCookieValor(String cookieValor) {
			this.cookieValor = cookieValor;
		}
		public String getCookieNombre() {
			return cookieNombre;
		}
		public void setCookieNombre(String cookieNombre) {
			this.cookieNombre = cookieNombre;
		}
		private String mensaje;
		private String cookieValor;
		private String cookieNombre;
	}
	
	//CON ESTE MÉTODO SE ORGANIZA EL ORDEN QUE DEBE HABER EN EL APPLICATION.YML.  
	public List<String>shortcutFieldOrder(){
		return Arrays.asList("mensaje","cookieNombre","cookieValor");
	}
	
	
	private final Logger logger = LoggerFactory.getLogger(EjemploGatewayFilterFactory.class);
}
