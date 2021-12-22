package com.springboot.app.item.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.springboot.app.item.models.Item;
import com.springboot.app.item.models.Producto;
import com.springboot.app.item.models.service.IItemService;

@RestController
public class ItemController {
	
	@GetMapping("/listar")
	public List<Item>listar(@RequestParam(name="nombre",required=false)String nombre,@RequestHeader(name="token-request",required=false) String token){
		System.out.println(nombre);
		System.out.println(token);
		return itemService.findAll();
	}
	
	@HystrixCommand(fallbackMethod="metodoAlternativo")
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable(value="id") Long id,  @PathVariable(value="cantidad") Integer cantidad) {
		return itemService.findById(id, cantidad); 
	}
	
	
	public Item metodoAlternativo(Long id, Integer cantidad) {
		Item item = new Item();
		Producto producto = new Producto();
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("No se puede cargar el nombre del producto");
		producto.setPrecio(0.00);
		item.setProducto(producto);
		return item;
	}
	
	
	@Autowired
	//@Qualifier("serviceRestTemplate")
	@Qualifier("serviceFeign")
	private IItemService itemService;

}
