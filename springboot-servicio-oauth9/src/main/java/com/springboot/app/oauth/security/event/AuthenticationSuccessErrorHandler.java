package com.springboot.app.oauth.security.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import com.springboot.app.commons.usuarios.models.entity.Usuario;
import com.springboot.app.oauth.services.IUsuarioService;

import feign.FeignException;

//CLASE 103 : SE CONSTRUYE ESTA CLASE PARA MANEJAR LOS CASOS DE EXITO Y ERROR EN LA AUTENTICACIÓN. POR EJEMPLO DESARROLLANDO ALGUNA TAREA DESPUÉS DE LA AUTENTICACIÓN.
//CON authentication : SE PUEDEM OBTENER LOS DATOS DEL CLIENTE QUE SE QUIERE O ESTA TRATANDO DE AUTENTICARSE.

//ESTA CLASE DE EVENTOS DEBE SER REGISTRADA EN SPRING SECURITY, EN LA CLASE SpringSecurityConfig DEL MICROSERVICIO OAUTH, DONDE SE INYECTA.

//1.-SE OBTIENEN LOS DATOS DEL USUARIO Y SE MUESTRAN EN CONSOLA. getPrincipal() : RETORNA EL USER DETAILS.
//2.-SE USA EXCEPTION PARA ENTREGAR MENSAJE DE ERROR.
//3.-(CLASE 104)PARA VERIFICAR QUE EL USUARIO AUTENTICADO NO ES PROPIO NUESTRO, SINO QUE ES EL FRONTEND APP.
//4.-(CLASE 105)SE INYECTA USUARIO SERVICE. ESTO PARA MANEJAR LOS INTENTOS DE INICIO DE SESIÓN. DE ESTA MANERA SE OBTINE AL USUARIO POR EL USERNAME.  Y SI NO SE OBTIENE AL USUARIO, SE MODIFICAN LOS INTENTOS QUE INICIALMENTE SON 3 Y SE ACTUALIZAN. 
	//SI SE INICIA SESIÓN ANTES DE LOS 3 INTENTOS, SE RESTABLECEN LA CANTIDAD DE INTENTOS A 3. 
	//SI NO SE LOGRA INICIAR SESIÓN DESPUÉS DE 3 INTENTOS, SE DESHABILITA LA CUENTA DEL USUARIO, MODIFICANDO EL ATRIBUTO ENABLED.
//5.-SE IMPLEMENTAN LOS INTENTOS EN EL CASO DE FRACAZO. 
	//5.1.-SE OBTIENE AL USUARIO CON EL USUARIO SERVICE. 
	//5.2.-CON EL IF, SE EVALÚA SI CON EL PRIMER INTENTO EL USUARIO HIZO LOGIN. SI EL LOGIN A LA PRIMERA FUE OK, ENTONCES LA CANTIDAD DE INTENTOS SE REESTABLECE A 0.
	//5.3.-usuario.setIntentos(usuario.getIntentos()+1); SI NO ENTRÓ EN EL IF() ENTONCES A INTENTOS SE LE SUMA 1.
	//5.4.-IF() SI LOS INTENTOS SON >=3, SE DESHABILITA AL USUARIO.
	//5.5.-FINALMENTE SE ACTUALIZA AL USUARIO MEDIANTE USUARIOSERVICE.
	//5.6.-CATCH: SI EL USUARIO NO EXISTE, LANZARÁ UN 404. ESTO SE MANEJA CON TRY CATCH.
//6.-EN CASO DE ÉXITO
	//SE ALMACENA EN USUARIO AL USUARIO QUE SE ESTÁ AUTENTICANDO.
	//IF() SE REINICIA EL CONTADOR DE INTENTOS.
	//SE ACTUALIZA AL USUARIO.

@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {
	
	//PARA MANEJAR EL ÉXITO
	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		//3
		if(authentication.getDetails() instanceof WebAuthenticationDetails) {
		//if(authentication.getName().equalsIgnoreCase("frontendapp")){
			return;
		}
		//1
		UserDetails user = (UserDetails) authentication.getPrincipal();
		System.out.println("Success login: " + user.getUsername());
		log.info("Success login: " + user.getUsername());
		
		//6
		Usuario usuario = usuarioService.findByUsername(authentication.getName());
		if(usuario.getIntentos() != null && usuario.getIntentos()>0) {
			usuario.setIntentos(0);
			usuarioService.update(usuario, usuario.getId());
		}
	}

	
	//PARA MANEJAR EL ERROR
	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		//2
		System.out.println("Error en el login: " + exception.getMessage());
		log.error("Error en el login: " + exception.getMessage());
		
		//5
		try {
			Usuario usuario = usuarioService.findByUsername(authentication.getName());
			
			if(usuario.getIntentos()== null) {
				usuario.setIntentos(0);
			}
			
			log.info("intentos ACTUALES son: " + usuario.getIntentos());
			usuario.setIntentos(usuario.getIntentos()+1);
			log.info("intentos DESPUÉS son: " + usuario.getIntentos());
			
			if(usuario.getIntentos()>=3) {
				log.error(String.format("Usuario %s deshabilitado por superar máximo de intentos.", usuario.getUsername()));
				usuario.setEnabled(false);
			}
			
			usuarioService.update(usuario, usuario.getId());
		
		}catch(FeignException e) {
			log.error(String.format("El usuario %s no existe en el sistema", authentication.getName()));
		}
	}

	
	private Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);
	
	
	//4
	@Autowired
	private IUsuarioService usuarioService;
}
