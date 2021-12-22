package com.springboot.app.item.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.app.item.models.Item;
import com.springboot.app.commons.models.entity.Producto;
import com.springboot.app.item.models.service.IItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

@RefreshScope //PARA RECONOCER LOS CAMBIOS EN TIEMPO REAL QUE SE PUEDAN ESTAR HACIENDO EN LOS PROPERTIES. FUNCIONA CON DEPENDENCIA SPRING ACTUATOR
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
	
	//RESPONSE ENTITY REPRESENTA EL CONTENIDO QUE SE GUARDARÁ EN LA RESPUESTA. AQUÍ SE GUARDARÁN LAS CONFIGURACIONES EN UN MAP
	@GetMapping("/obtener-config")
	public ResponseEntity <?> obtenerConfig(@Value("${server.port}")String puerto){
		logger.info(texto);
		Map<String,String> json = new HashMap<>();
		json.put("texto", texto);
		json.put("puerto", puerto);
		
		//PARA VER SI VIENE ALGO EN EL ARREGLO. DE ESTA MANERA SE SABE SI HAY CONFIGURADO ALGÚN PROFILE. Y SE PREGUNTA SI LA CONFIGURACION ES DE DESARROLLO.
		if(env.getActiveProfiles().length>0 && env.getActiveProfiles()[0].equals("dev")) {
			//SI ESTAMOS EN DESARROLLO, SE AGREGA NOMBRE Y AUTOR AL JSON
			json.put("autor.nombre", env.getProperty("configuracion.autor.nombre"));
			json.put("autor.email", env.getProperty("configuracion.autor.email"));
		}
		return new ResponseEntity <Map<String,String>>(json, HttpStatus.OK);
	}
	
	
	@PostMapping("/crear")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto crear(@RequestBody Producto producto) {
		return itemService.save(producto);
	}
	
	@PutMapping("/editar/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Producto editar(@RequestBody Producto producto, @PathVariable Long id) {
		return itemService.update(producto, id);
	}
	
	@DeleteMapping("/eliminar/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void eliminar(@PathVariable Long id) {
		itemService.delete(id);
	}
	
	@Autowired
	//	@Qualifier("serviceRestTemplate")
	@Qualifier("serviceFeign")
	private IItemService itemService;
	
	@Autowired
	private CircuitBreakerFactory cbFactory;
	
	@Value("${configuracion.texto}")
	private String texto;
	
	private static Logger logger = LoggerFactory.getLogger(ItemController.class);
	
	@Autowired
	private Environment env;

}
