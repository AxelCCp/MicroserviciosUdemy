package com.springboot.app.oauth.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;


//1.- CLASE DE CONFIGURACION DEL SERVIDOR DE AUTORIZACIÓN. SE ENCARGA DE: LOGIN POR PARTE DE OAUTH2, PROCESO DE CREACIÓN DE TOKEN JWT Y VALIDACIÓN. PARA ESTO SE USA EL AUTHENTICATION MANAGER.
//2.- SE HEREDA DE LA CLASE DE CONFIGURACIÓN AuthorizationServerConfigurerAdapter Y SE USAN 3 DE SUS MÉTODOS.
//3.- EL authenticationManager ES REGISTRADO EN EL AuthorizationServerConfigurerAdapter.
//AQUÍ SE CONFIGURA EL AuthenticationManager. 
//TAMBN EL TOKEN STORAGE QUE ES DEL TIPO JWT.
//TAMBN EL CONVERTER QUE GUARDA LOS DATOS DEL USUARIO EN EL TOKEN.
//3.- AUTOWIRED DE BCryptPasswordEncoder Y AuthenticationManager.
//4.- REGISTRO DE AuthenticationManager.
//5.- CONFIGURACIÓN DEL TOKEN STORAGE. COMPONENTE QUE GENERA Y GUARDA EL TOKEN.
//6.- ESTE MÉTODO LO CREA UNO Y DEBE SER PUBLIC Y SE CONFIGURA PARA QUE SEA DEL TIPO JWT.
//7.- SE AGREGA CÓDIGO SECRETO QUE ES PARA LA FIRMA DEL TOKEN.
//8.- SE CREA EL TOKEN STORAGE. EL COMPONENTE QUE GUARDA EL TOKEN CON LOS DATOS DEL accessTokenConverter. EL accessTokenConverter GUARDA LOS DATOS DEL USUARIO EN EL TOKEN, ETC.
//9.- SE CREA EL MÉTODO DE TIPO PUBLIC PARA DEVOLVER UN OBJ TOKENSTORE, AL CUAL SE LE PASA EL MÉTODO QUE DEVUELVE UN tokenConverter. 
//POR LO TANTO EL TOKENSTORE RECIBE POR PARAMETRO EL MÉTODO QUE CREA AL TOKEN JWT CON SU RESPECTIVA INFORMACIÓN DE CONFIGURACIÓN.
//10.-CONFIGURACIÓN DE LOS CLIENTES(APPS FRONTEND) EN EL SERVIDOR DE AUTORIZACIÓN.
//11.-withClient():SE REGISTRA UN CLIENTE EN MEMORIA. 
		//secret(): TAMBN SE REGISTRA SU CONTRASEÑA QUE ES ENCRIPTADA CON EL BEAN passwordEncoder Y EL MET.encode().
		//scopes("read","write"): SE CONFIGURA EL ALCANCE DE LA APPLICATION(LEER Y ESCRIBIR DATOS).
		//authorizedGrantTypes(): DEFINE EL TIPO DE CONSESIÓN QUE VA A TENER LA AUTENTICACIÓN.O SEA CÓMO SE VA A OBTENER EL TOKEN.EN ESTE CASO CON PASSWORD.
		//                        SE AGREGA TAMBN REFRESHTOKEN, PERMITE LA RENOVACIÓN DE TOKEN DESPUÉS DEL TIEMPO DE VALIDEZ DEL TOKEN.
		//accessTokenValiditySeconds(1HORA): TIEMPO DE VALIDEZ DEL TOKEN.
		//refreshTokenValiditySeconds(): TIEMPO DE REFRESH DEL TOKEN.
		
		//and(): CON AND() SE PUEDE CONFIGURAR OTRA APPLICATION CLIENTE.
//12.-PERMISO QUE VAN A TENER LOS ENDPOINTS DEL SERVIDOR DE AUTORIZACIÓN DE OAUTH2, PARA GENERAR Y VALIDAR EL TOKEN.
	//tokenKeyAccess(): ES EL ENDPONT PARA GENERAR EL TOKEN. PARA AUTENTICARNOS CON LA RUTA: "POST:/oauth/token"
	//permitAll: ES UN PERMISO DE SPRING SECURIRY PARA PERMITIR A TODOS.
	//checkTokenAccess():RUTA PARA VALIDAR EL TOKEN. isAuthenticated() : METODO DE SPRING SECURITY PARA VALIDAR QUE EL CLIENTE ESTÉ AUTENTICADO.
	//tokenKeyAccess Y checkTokenAccess: SON ENDPOINTS QUE ESTÁN PROTEGIDOS POR AUTENTICACIÓN VÍA HTTP BASIC. USANDO LAS CREDENCIALES DEL CLIENTE CORRESPONDIENTE.
//13.- SE UNE LA INFORMACIÓN ADICIONAL CONFIGURADA EN LA CLASE InfoAdicionalToken A TOKEN.
	//TokenEnhancerChain : ES UNA CLASE CADENA QUE UNE LA INFO ADICIONAL A LA INFO QUE YA ESTÁ EN EL TOKEN.
	//setTokenEnhancers() : RECIBE UNA LISTA, POR LO TANTO SE UNA Arrays.toList(). Y DENTRO DE ESTOS PARÉNTESIS SE UNE LA INFORMACIÓN ADICIONAL.
//14.- SE AGREGA LA CADENA DE "LA INFORMACIÓN ADICIONAL + INFO TOKEN"  AL CONFIGURACIÓN DEL ENDPOINT.
//15.-(CLASE 100) SE INYECTA ENVIRONTMENT.

//1.-
@RefreshScope  // CON ESTA SE PUEDEN MODIFICAR DATOS EN EL ARCHIVO DEL REPOSITORIO GITHUB, MIESTRAS EL MICROSERVICIO ESTÁ LEVANTADO.
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
//2.-

	//12.-
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")
		.checkTokenAccess("isAuthenticated()");
	}
	
	
	//10.-
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//11.-
		clients.inMemory().withClient(env.getProperty("config.security.oauth.client.id"))
						.secret(passwordEncoder.encode(env.getProperty("config.security.oauth.client.secret"))).scopes("read","write")
						.authorizedGrantTypes("password","refresh_token")
						.accessTokenValiditySeconds(3600)
						.refreshTokenValiditySeconds(3600);
	}
	
	//3.-
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		//13.-
		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken,accessTokenConverter()));
		//4.-
		endpoints.authenticationManager(authenticationManager)
		//8.-
		.tokenStore(tokenStore())
		//5.-
		.accessTokenConverter(accessTokenConverter())
		//14.-
		.tokenEnhancer(tokenEnhancerChain);
		
	}
	
	//6.-
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter tokenConverter = new JwtAccessTokenConverter();
		//7.- SE AGREGA CÓDIGO SECRETO QUE ES PARA LA FIRMA DEL TOKEN
		tokenConverter.setSigningKey(env.getProperty("config.security.oauth.jwt.key"));
		return tokenConverter;
	}
	
	//9.-
	@Bean
	public JwtTokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}

	//3.-
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private AuthenticationManager authenticationManager;
	//13.-
	@Autowired
	private InfoAdicionalToken infoAdicionalToken;
	//15.-
	@Autowired
	private Environment env;
}
