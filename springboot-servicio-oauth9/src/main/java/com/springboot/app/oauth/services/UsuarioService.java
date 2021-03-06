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

//1.-ESTA ES LA CLASE SERVICE QUE SE ENCARGA DE AUTENTICAR AL USUARIO. IMPLEMENTA UNA INTERFAZ PROPIA DE SPRING SECURITY QUE ES DE AUTENTICACIÓN.

@Service
public class UsuarioService implements UserDetailsService, IUsuarioService {
	
	//3.- ESTE MÉTODO DEVUELVE UN OBJ USERDETAILS Y OBTIENE AL USUARIO A TRAVÉS DEL USERNAME. LA CLASE USERDETAILS REPRESENTA A UN USUARIO AUTENTICADO POR SPRING SECURITY.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = client.findByUsername(username);
		
		//4.-SI NO EXISTE EL USUARIO, LANZA UN ERROR.
		if(usuario==null) {
			log.error("ERROR EN EL LOGIN. NO EXISTE EL USUARIO '" + username + "' EN EL SISTEMA");
			throw new UsernameNotFoundException("ERROR EN EL LOGIN. NO EXISTE EL USUARIO '" + username + "' EN EL SISTEMA");
		}
		
		//5.-SE OBTIENEN LOS ROLES DEL USUARIO ENCONTRADO.
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
	
	//(CLASE 97) 6.-
	@Override
	public Usuario findByUsername(String username) {
		// TODO Auto-generated method stub
		return client.findByUsername(username);
	}
	
	//7.-(CLASE 105) SE IMPLEMENTA NUEVO MÉTODO DE LA INTERFAZ. 
	@Override
	public Usuario update(Usuario usuario, Long id) {
		// TODO Auto-generated method stub
		return client.update(usuario, id);
	}
	
	
	//2.-SE INYECTA BEAN DESDE LA INTERFAZ CLIENTE FEIGN QUE CONECTA CON USUARIO COMMONS
	@Autowired
	private UsuarioFeignClient client;
	private Logger log = LoggerFactory.getLogger(UsuarioService.class);
	

}
