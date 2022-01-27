package com.springboot.app.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

//1.-SE USA PARA HABILITAR LA SEGURIDAD EN WEBFLUX.
//ESTA CLASE NO IMPLEMENTA NADA. CON ESTA ANNOTATION DE CONFIGURACIÓN, SE TENDRÁ UN MÉTODO BEAN QUE REGISTRA UN COMPONENTE. LA CLASE SecurityWebFilterChain, PARA CONFIGURAR TODO CON RESPECTO A SEGURIDAD.	

//2.- IMPLEMENTACIÓN DE MÉTODO CON @BEAN
	//ServerHttpSecurity http : ESTA CLASE ES PARA CONFIGURAR LA SEGURIDAD BASADA EN LA WEB PARA LAS SOLICITUDES HTTP ESPECIFICAS. "ServerHttpSecurity" ES PARA WEBFLUX Y ES SIMILAR A LA "HttpSecurity" DE SPRING SECURITY. 
	//authorizeExchange():PARA CONFIGURAR LA AUTORIZACIÓN.
	//anyExchange(): SE HACE REFERENCIA A CUALQUIER RUTA. 
	//authenticated(): HACE REFERENCIA A QUE REQUIERE UN USUARIO AUTENTICADO.
	//and(): PERMITE ENCADENAR MÉTODOS PARA SEGUIR CONFIGURANDO ServerHttpSecurity.
	//csrf():CONFIGURA LA PROTECCIÓN "CSRF" QUE YA ESTA HABILITADA DE FORMA PREDETERMINADA.
	//disable(): DESHABILITA LA PROTECCIÓN "CSRF". SE DESHABILITA EL TOKEN CSRF QUE ES PARA VISTA DE FORMULARIO CON HTML.
	//build(): CONSTRÚYELO.

//3.-SE AGREGAN PERMISOS A LAS RUTAS
	//pathMatchers("/api/security/oauth/**").permitAll(): PARA CUALQUIER RUTA DE OAUTH SE DEJA ACCESO PÚBLICO.
	//hasAnyRole("ADMIN","USER") : AQUÍ SE AGREGA EL PERMISO PARA 2 ROLES.
	//RUTAS CON POST, PUT Y DELETE, SERÁN RUTAS PRIVADAS. DESPUES DE CONFIGURAR LAS RUTAS PÚBLICAS, QUEDAN LAS PRIVADAS. Y POR SER EL RESTO QUE QUEDA, SE USAN **. 
//1
@EnableWebFluxSecurity
public class SpringSecurityConfig {
	
	//2
	@Bean
	public SecurityWebFilterChain configure(ServerHttpSecurity http) {
		return http.authorizeExchange()
				//3.1
				.pathMatchers("/api/security/oauth/**").permitAll()
				//3.2
				.pathMatchers(HttpMethod.GET, "/api/productos/listar", 
												"/api/items/listar", 
												"/api/usuarios/usuarios", 
												"/api/items/ver/{id}/cantidad/{cantidad}",
												"/api/productos/ver/{id}").permitAll()
				//3.3
				.pathMatchers(HttpMethod.GET, "/api/usuarios/usuarios/{id}").hasAnyRole("ADMIN","USER")
				//3.4
				.pathMatchers("/api/productos/**", "/api/items/**", "api/usuarios/usuarios/**").hasAnyRole("ADMIN")
				
				//2
				.anyExchange()
				.authenticated()
				.and().csrf().disable()
				.build();
	}
	
}
