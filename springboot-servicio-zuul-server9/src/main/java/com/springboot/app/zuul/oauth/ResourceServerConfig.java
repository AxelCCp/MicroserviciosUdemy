package com.springboot.app.zuul.oauth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
//10.-SE INYECTA VALUE, AUNQUE TAMBN PUEDE SER CON ENVIRONTMENT. ESTO YA QUE SE INYECTA UNA SOLA PROPIEDAD. 
//11.-SE REEMPLAZA CON jwtKey. 
//12.-(CLASE 102) CONFIGURACIÓN DEL CORS. EL CORS SE USA PARA QUE LAS APPLICACIONES CLIENTES QUE ESTÁN EN OTRO SERVIDOR O DOMINIO SE PUEDAN CONECTAR AL BACKEND. CORS OCUPA LAS CABECERAS HTTP PARA QUE LAS APP CLIENTES PUEDAN ACCEDER AL BACKEND.
	//EL CORS SE CONFIGURA EN LAS RUTAS PROTEGIDAS.
//13.-SE CREA MÉTODO corsConfigurationSource()
	//13.1.-CONFIGURACIÓN DE LAS APPLICACIONES CLIENTES. LA UBICACIÓN, EL NOMBRE DE DOMINIO Y PUERTO. setAllowedOrigins(Arrays.asList("*") : RECIBE UNA LISTA DE LAS APPS CLIENTES. EN ESTE CASO CON ASTERISCO SE PERMITE EL ACCESO A TODAS LAS APPS.
	//13.2.-HAY QUE PERMITIR LOS MÉTODOS QUE ESTAN PERMITIDOS A LAS APPS CLIENTES. MÉTODOS POST, GET, PUT, DELETE, UPDATE, ETC.
	//13.3.-setAllowedHeaders() : LAS CABECERAS SEPARADAS POR COMAS EN UN ARRAY LIST. "Authorization" : PARA CUANDO SE ENVÍA EL TOKEN EN LAS CABECERAS PARA ACCEDER A LOS RECURSOS.
	//13.4.- SE PASA LA CONFIGURACIÓN DEL "corsConfig" A LAS RUTAS URL. A LOS ENDPOINTS. "/**" : CON ESTO SE LE PASA A TODAS LAS RUTAS.
//14.-SE CREA OTRO BEANS. OTRO COMPONENTE PARA REGISTRAR UN FILTRO DE CORS. ESTO PARA QUE NO SOLO QUEDE REGISTRADO EN SPRING SECURITY. SINO QUE TAMBN QUEDE REGISTRADO A NIVEL GLOBAL EN UN FILTRO DE SPRING. ESTA CONFIGURACIÓN GLOBAL ES OPCIONAL, SE USA SOLO SI LAS APPS CLIENTES ESTÁN EN OTRO DOMINIO.
	//14.1.-SE CREA UNA INSTANCIA "BEAN" DE TIPO FilterRegistrationBean<CorsFilter>. SE LE PASA POR EL CONSTRUCTOR UNA INSTANCIA CorsFilter, QUE RECIBE EN SU CONSTRUCTOR EL MÉTODO QUE DEVUELVE AL OBJ "source". DE ESTA MANERA SE LE PASA LA CONFIGURACIÓN.
	//14.2.-SE LE ESTABLECE UNA PRIORIDAD ALTA.
	//14.3.-EL OBJ BEAN REGISTRA EL CORSFILTER.

@RefreshScope  // CON ESTA SE PUEDEN MODIFICAR DATOS EN EL ARCHIVO DEL REPOSITORIO GITHUB, MIESTRAS EL MICROSERVICIO ESTÁ LEVANTADO.
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
		.antMatchers(HttpMethod.DELETE, "/api/productos/eliminar/{id}", "/api/items/eliminar/{id}", "/api/usuarios/usuarios/{id}").hasRole("ADMIN")
		//12 CORS
		.and().cors().configurationSource(corsConfigurationSource());
	}
	
	//13
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		//13.1
		corsConfig.setAllowedOrigins(Arrays.asList("*"));
		//13.2
		corsConfig.setAllowedMethods(Arrays.asList("POST","GET","PUT","DELETE","OPTIONS"));
		corsConfig.setAllowCredentials(true);
		//13.3
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
		//13.4
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}
	
	//14 
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter(){
		//14.1
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		//14.2
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		//14.3
		return bean;
	}

	//3
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		//7.- SE AGREGA CÓDIGO SECRETO QUE ES PARA LA FIRMA DEL TOKEN   // 11.- 
		tokenConverter.setSigningKey(jwtKey);
		return tokenConverter;
	}
	//3
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	//10
	@Value("${config.security.oauth.jwt.key}")
	private String jwtKey;

}
