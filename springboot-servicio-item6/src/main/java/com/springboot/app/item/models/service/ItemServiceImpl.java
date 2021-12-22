package com.springboot.app.item.models.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.springboot.app.item.models.Item;
import com.springboot.app.item.models.Producto;

@Service("serviceRestTemplate")
public class ItemServiceImpl implements IItemService {

	@Override
	public List<Item> findAll() {
		//SE HACE ASÍ CUANDO EL BALANCEO DE CARGA ES CON FEIGN
		//List<Producto>productos = Arrays.asList(clienteRest.getForObject("http://localhost:8001/listar", Producto[].class)); 
		
		//SE HACE ASÍ CON EL BALANCEO DE CARGA CON REST TEMPLATE. SE RELACIONA CON EL @LOADBALANCED EN LA CLASE APPCONFIG
		List<Producto>productos = Arrays.asList(clienteRest.getForObject("http://servicio-productos/listar", Producto[].class)); 
		return productos.stream().map(p-> new Item(p,1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		// TODO Auto-generated method stub
		Map<String,String>pathVariables = new HashMap<String,String>();  
		pathVariables.put("id", id.toString());
		//SE HACE ASÍ CUANDO EL BALANCEO DE CARGA ES CON FEIGN
		//Producto producto = clienteRest.getForObject("http://localhost:8001/ver/{id}", Producto.class, pathVariables);
		
		//SE HACE ASÍ CON EL BALANCEO DE CARGA CON REST TEMPLATE. SE RELACIONA CON EL @LOADBALANCED EN LA CLASE APPCONFIG
		Producto producto = clienteRest.getForObject("http://servicio-productos/ver/{id}", Producto.class, pathVariables);
		return new Item(producto,cantidad);
	}


	@Override
	public Producto save(Producto producto) {
		// Exchange(): DEVUELVE UN RESPONSEENTITY. INTERCAMBIAR. SE CONSUME UN API REST. SE ENVIA EN UN ENDPOINT UN OBJ PRODUCTO EN EL BODY DE LA RESPUESTA.
		//exchange(RUTA, TIPO DE MÉTODO, EL REQUEST CON EL OBJ PRODUCTO, TIPO DE DATO)
		//EL HTTPENTITY SE ENVÍA EN EL BODY CON POST
		HttpEntity<Producto>body = new HttpEntity<Producto>(producto);
		ResponseEntity<Producto>response = clienteRest.exchange("http://servicio-productos/crear", HttpMethod.POST, body, Producto.class);
		Producto productoResponse = response.getBody();
		return productoResponse;
	}

	@Override
	public Producto update(Producto producto, Long id) {
		// TODO Auto-generated method stub
		Map<String,String>pathVariables = new HashMap<String,String>();  
		pathVariables.put("id", id.toString());
		HttpEntity<Producto>body = new HttpEntity<Producto>(producto);
		ResponseEntity<Producto>response = clienteRest.exchange("http://servicio-productos/editar/{id}", HttpMethod.PUT,body,Producto.class,pathVariables);
		return response.getBody();
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		Map<String,String>pathVariables = new HashMap<String,String>();  
		pathVariables.put("id", id.toString());
		clienteRest.delete("http://servicio-productos/eliminar/{id}",pathVariables);
	}
	
	@Autowired
	private RestTemplate clienteRest;
	
}
