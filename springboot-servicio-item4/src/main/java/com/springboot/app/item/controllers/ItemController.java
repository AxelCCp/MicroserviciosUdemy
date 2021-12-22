package com.springboot.app.item.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.app.item.models.Item;
import com.springboot.app.item.models.Producto;
import com.springboot.app.item.models.service.IItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RestController
public class ItemController {
	
	@GetMapping("/listar")
	public List<Item>listar(@RequestParam(name="nombre",required=false)String nombre,@RequestHeader(name="token-request",required=false) String token){
		System.out.println(nombre);
		System.out.println(token);
		return itemService.findAll();
	}
	
	
	@GetMapping("/ver/{id}/cantidad/{cantidad}")
	public Item detalle(@PathVariable(value="id") Long id,  @PathVariable(value="cantidad") Integer cantidad) {
		return cbFactory.create("items")
				.run(()->itemService.findById(id, cantidad), e-> metodoAlternativo(id,cantidad,e)); 
	}
	
	public Item metodoAlternativo(Long id, Integer cantidad, Throwable e) {
		logger.info(e.getMessage());
		Item item = new Item();
		Producto producto = new Producto();
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("No se puede cargar el nombre del producto");
		producto.setPrecio(0.00);
		item.setProducto(producto);
		return item;
	}
	
	
	@CircuitBreaker(name="items", fallbackMethod="metodoAlternativo")
	@GetMapping("/ver2/{id}/cantidad/{cantidad}")
	public Item detalle2(@PathVariable(value="id") Long id,  @PathVariable(value="cantidad") Integer cantidad) {
		return itemService.findById(id, cantidad); 
	}
	
	@CircuitBreaker(name="items",fallbackMethod="metodoAlternativo2")
	@TimeLimiter(name="items")
	@GetMapping("/ver3/{id}/cantidad/{cantidad}")
	public CompletableFuture<Item> detalle3(@PathVariable(value="id") Long id,  @PathVariable(value="cantidad") Integer cantidad) {
		return CompletableFuture.supplyAsync(()->itemService.findById(id, cantidad)) ; 
	}
	public CompletableFuture<Item> metodoAlternativo2(Long id, Integer cantidad, Throwable e) {
		logger.info(e.getMessage());
		Item item = new Item();
		Producto producto = new Producto();
		item.setCantidad(cantidad);
		producto.setId(id);
		producto.setNombre("No se puede cargar el nombre del producto");
		producto.setPrecio(0.00);
		item.setProducto(producto);
		return CompletableFuture.supplyAsync(()->item);
	}
	
	
	@Autowired
	//@Qualifier("serviceRestTemplate")
	@Qualifier("serviceFeign")
	private IItemService itemService;
	
	@Autowired
	private CircuitBreakerFactory cbFactory;
	
	private final Logger logger = LoggerFactory.getLogger(ItemController.class);

}
