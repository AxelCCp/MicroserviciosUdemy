package com.springboot.app.usuarios;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import com.springboot.app.commons.usuarios.models.entity.Usuario;
import com.springboot.app.commons.usuarios.models.entity.Role;

//PARA LA CONFIGURACIÓN DE USUARIO Y ROLE, PARA QUE SE MUESTREN LOS ID EN EL JSON.
//SE IMPLEMENTA PARA CONFIGURAR LOS ID Y CUALQUIER CONFIGURACION CON EL REPOSITORIO REST.

@Configuration
public class RepositoryConfig implements RepositoryRestConfigurer{

	//CONFIG: ES PARA CONFIGURAR EL REPOSITORIO REST.
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
		config.exposeIdsFor(Usuario.class,Role.class);
	}
	
}
