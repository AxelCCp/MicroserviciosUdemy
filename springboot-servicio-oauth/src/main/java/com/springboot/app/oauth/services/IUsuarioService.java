package com.springboot.app.oauth.services;


import com.springboot.app.commons.usuarios.models.entity.Usuario;

//2.-CLASE 105.- SE AGREGA MÉTODO QUE TIENE LA FINALIDAD DE CONTAR LOS INTENTOS DE INICIO DE SESIÓN.

public interface IUsuarioService {
	
	public Usuario findByUsername(String username);
	
	
	
}
