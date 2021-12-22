package com.springboot.app.productos.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.springboot.app.productos.models.entity.Producto;
import com.springboot.app.productos.models.service.IProductoService;

@RestController
public class ProductoController {
	
	@GetMapping("/listar")
	public List<Producto>listar(){
		return productoService.findAll().stream().map(producto->{
			//producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));
			producto.setPort(port);
			return producto;
		}).collect(Collectors.toList());
	}
	
	
	@GetMapping("/ver/{id}")
	public Producto detalle(@PathVariable(value="id")Long id) {
		Producto producto = productoService.findById(id);
		producto.setPort(Integer.parseInt(env.getProperty("local.server.port")));
		
		/*//PARA PROBAR METODO FALLBACK DEL CONTROLADOR DE ITEM
		boolean ok = false;
		if(!ok) {
			throw new RuntimeException("No se pudo cargar el producto!");
		}*/
		
		/*//PARA PROBAR EL TIME OUT DE HYSTRIX
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return producto;
	}
	
	
@Autowired
private IProductoService productoService;

@Autowired
private Environment env;

@Value("${server.port}")
private Integer port;
}