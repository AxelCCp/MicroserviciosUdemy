package com.springboot.app.usuarios.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import com.springboot.app.commons.usuarios.models.entity.Usuario;


//SE AUTOMATIZA LA CREACIÓN DE UN CONTROLADOR. ESTA ETIQUETA VIENE DE LA DEPENDENCIA spring-boot-starter-data-rest.
//CON EL PATH SE HACE REFERENCIA A DÓNDE SE VA A EXPORTAR EL CRUDREPOSITORY.
@RepositoryRestResource(path="usuarios")
public interface IUsuarioDao extends PagingAndSortingRepository <Usuario, Long>{
	
	//QUERY SEGÚN SPRING DATA JPA
	//localhost:8090/api/usuarios/usuarios/search/buscar-username?username=RRRR
	@RestResource(path="buscar-username")
	public Usuario findByUsername(@Param("username") String username);
	
	//CONSULTA PERSONALIZADA. ?1 HACE REFERENCIA A UN PARÁMETRO
	//localhost:8090/api/usuarios/usuarios/search/obtenerPorUsername?username=RRRR
	@Query("select u from Usuario u where u.username=?1")
	public Usuario obtenerPorUsername(String username);
}
