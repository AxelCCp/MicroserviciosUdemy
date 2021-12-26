package com.springboot.app.oauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//1.-CLASE PRINCIPAL DE SPRING SECURITY. ESTA ES LA CLASE DE CONFIGURACIÓN DEL AUTHENTICATION MANAGER.
//WebSecurityConfigurerAdapter ES PARA CONFIGURAR EL USER DETAIL SERVICE.

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	//2.-SE HEREDAN 2 MÉTODOS
	
	//3.-MÉTODO PARA REGISTRAR EL usuarioService EN EL AUTHENTICATION MANAGER.
	//auth: SE INYECTA UN OBJ AuthenticationManagerBuilder, POR LO TANTO SE USA AUTOWIRED, PARA QUE SE REALICE LA INYECCIÓN A TRAVÉS DEL MÉTODO.
	@Override
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//SE REGISTRA EL USER DETAIL SERVICE Y SE LE PASA COMO REFERENCIA usuarioService. TAMBIÉN SE ENCRIPTA LA CONTRASEÑA.
		auth.userDetailsService(this.usuarioService).passwordEncoder(passwordEncoder());
	}
	
	//5.-CONFIGURACION DEL AUTHENTICATION MANAGER. AQUI SE REGISTRA COMO UN COMPONENTE DE SPRING, PARA USARLO CON INYECCIÓN EN EL SERVIDOR OAUTH2 
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManager();
	}
	

	//4.- BCrypt: ES UN ENCRIPTADOR DE PASSWORD QUE COMPARA LA PASSWORD ENCRIPTADA Y REGISTRADA EN LA BBDD, CON LA PASSWORD INGRESADA POR EL USUARIO, QUE TAMBN ES ENCRIPTADA AL INICIAR SESIÓN. 
	//@Bean: SE GUARDA EN EL CONTENEDOR DE SPRING PARA ENCRIPTAR LAS CONTRASEÑAS.
	@Bean 
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
		
	
	@Autowired 
	private UserDetailsService usuarioService;
	
}
