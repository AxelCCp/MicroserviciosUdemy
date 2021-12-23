package com.springboot.app.oauth.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot.app.commons.usuarios.models.entity.Usuario;
import com.springboot.app.oauth.clients.UsuarioFeignClient;

//IMPLEMENTA INTERFAZ PROPIA DE SPRING SECURITY QUE ES DE AUTENTICACIÓN.
@Service
public class UsuarioService implements UserDetailsService {
	//ESTE MÉTODO DEVUELVE UN OBJ USERDETAILS Y OBTIENE AL USUARIO A TRAVÉS DEL USERNAME. LA CLASE USERDETAILS REPRESENTA A UN USUARIO AUTENTICADO POR SPRING SECURIRTY.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = client.findByUsername(username);
		
		if(usuario==null) {
			log.error("ERROR EN EL LOGIN. NO EXISTE EL USUARIO '" + username + "' EN EL SISTEMA");
			throw new UsernameNotFoundException("ERROR EN EL LOGIN. NO EXISTE EL USUARIO '" + username + "' EN EL SISTEMA");
		}
		
		//AUTHORITIES REPRESENTA A LOS ROLES QUE TIENE EL USUARIO. SON UNA LISTA MANY TO MANY Y EN SPRING SECURITY LA LISTA ES DEL TIPO GENÉRICO GrantedAuthority.
		//LA LISTA DEL TIPO GENÉRICO GrantedAuthority SE LLENA CON ROLES DEL TIPO DE LA CLASE SimpleGrantedAuthority. ES POR ESTO Q SE HACE UNA CONVERSIÓN. 
		//PEEK(): PARA MOSTRAR EL ROLE DEL USUARIO AUTENTICADO.
		List<GrantedAuthority>authorities =usuario.getRoles()
				.stream().map(role -> new SimpleGrantedAuthority(role.getNombre()))
				.peek(authority -> log.info("Role: " + authority.getAuthority()))
				.collect(Collectors.toList());
		//MUESTRA EL NOMBRE DEL USUARIO AUTENTICADO:
		log.info("USUARIO AUTENTICADO: " + username);
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true, authorities);
	}
	
	//SE INYECTA BEAN DESDE LA INTERFAZ CLIENTE FEIGN QUE CONECTA CON USUARIO COMMONS
	@Autowired
	private UsuarioFeignClient client;
	
	private Logger log = LoggerFactory.getLogger(UsuarioService.class);

}