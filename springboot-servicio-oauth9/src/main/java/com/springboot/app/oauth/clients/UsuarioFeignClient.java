package com.springboot.app.oauth.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot.app.commons.usuarios.models.entity.Usuario;

//1.- EL CLIENTE FEIGN SE CONECTA CON EL MICROSERVICIO SERVICIO-USUARIOS VERSIÓN 8.1. EN LA CLASE PRINCIPAL SE PONE @ENABLEFEIGNCLIENT
// @RequestParam : SE ENVÍA EL PARÁMETRO USERNAME DESDE microservicio-oauth PARA PODER CONSUMIR EL MICROSERVICIO servicio-usuarios

//2.-(CLASE 105)ESTE MÉTODO ES PARA ACTUALIZAR. PARA CONTABILIZAR EL NÚMERO DE INTENTOS DE INICIO DE SESIÓN.

@FeignClient(name="servicio-usuarios")
public interface UsuarioFeignClient {
	
	
	@GetMapping("/usuarios/search/buscar-username")
	public Usuario findByUsername(@RequestParam("username") String username);
	

	//2
	@PutMapping("/usuarios/{id}")
	public Usuario update(@RequestBody Usuario usuario, @PathVariable Long id);
}
