package com.springboot.app.zuul.oauth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

//ESTA  ES LA CLASE DONDE SE CREA Y CONFIGURA EL SERVIDOR DE RECURSOS

//1.-HEREDA ResourceServerConfigurerAdapter.
//2.-SE SOBREESCRIBEN LOS MÉTODOS DE LA CLASE ResourceServerConfigurerAdapter:  
	//SE CONFIGURAN AQUI EL TOKEN STORE Y EL TOKEN CONVERTER, DE LA MISMA MANERA EN QUE SE CONFIGURÓ EN EL SERVIDOR DE AUTORIZACIÓN.
	//configure(ResourceServerSecurityConfigurer resources) : PARA CONFIGURAR EL TOKEN CON LA MISMA ESTRUCTURA DEL SERVIDOR DE AUTORIZACIÓN.
	//configure(HttpSecurity http) : PARA PROTEGER LAS RUTAS Y LOS ENDPOINTS.
//3.-SE COPIAN LOS BEAN'S DESDE EL SERVIDOR DE AUTORIZACIÓN.
//4.-CONFIGURACIÓN DE TOKEN STORE.
//5.-SE PROTEGEN LOS ENPOINTS. CADA RUTA DE ZUULSERVER.
	//authorizeRequests() : PARA PROTEGER ENDPOINT.
	//antMatchers() : SE PONE LA RUTA PARA GENERAR TOKEN. // RUTA PARA TOKEN: "/api/security/oauth/token"  //  RUTA PARA TOKEN UN OTRO : "/api/security/oauth/**"     
	//permitAll() : TIPO DE PERMISO PARA RUTA PÚBLICA. ESTE MÉTODO PERMITE QUE CUALQUIER USUARIO PUEDA INICIAR SESIÓN.
//6.-SE CREA OTRA RUTA PÚBLICA PARA LISTADO DE PRODUCTOS Y LISTADO DE USUARIOS.
//7.-RUTAS PARA VER DETALLE.
	//hasAnyRole() : SE ESTABLECEN LOS ROLES DE LAS PERSONAS QUE PUEDEN VER ESTA INFORMACIÓN. NO SE NECESITA PONERLES EL PREFIJO "ROLE_..."
//8.-RUTAS PARA HACER CRUD CREAR.
//9.-RUTAS PARA HACER CRUD MODIFICAR.
//9.-RUTAS PARA HACER CRUD ELIMINAR.
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		//4
		resources.tokenStore(tokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		//5
		http.authorizeRequests().antMatchers("/api/security/oauth/**").permitAll()
		//6
		.antMatchers(HttpMethod.GET, "/api/productos/listar", "/api/items/listar", "/api/usuarios/usuarios").permitAll()
		//7
		.antMatchers(HttpMethod.GET, "/api/productos/ver/{id}", "/api/items/ver/{id}/cantidad/{cantidad}", "/api/usuarios/usuarios/{id}").hasAnyRole("ADMIN","USER")
		//8
		.antMatchers(HttpMethod.POST, "/api/productos/crear", "/api/items/crear", "/api/usuarios/usuarios").hasRole("ADMIN")
		//9
		.antMatchers(HttpMethod.PUT, "/api/productos/editar/{id}", "/api/items/editar/{id}", "/api/usuarios/usuarios/{id}").hasRole("ADMIN")
		//10
		.antMatchers(HttpMethod.DELETE, "/api/productos/eliminar/{id}", "/api/items/eliminar/{id}", "/api/usuarios/usuarios/{id}").hasRole("ADMIN");
	}
	
	//3
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		//7.- SE AGREGA CÓDIGO SECRETO QUE ES PARA LA FIRMA DEL TOKEN
		tokenConverter.setSigningKey("algun_codigo_secreto_aeiou");
		return tokenConverter;
	}
	//3
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

}
