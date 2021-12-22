package com.springboot.app.item.models.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.springboot.app.item.clientes.IProductoClienteRest;
import com.springboot.app.item.models.Item;
import com.springboot.app.item.models.Producto;

@Service("serviceFeign")
@Primary
public class ItemServiceFeign implements IItemService {

	@Override
	public List<Item> findAll() {
		// TODO Auto-generated method stub
		return clienteFeign.listar().stream().map(p-> new Item(p,1)).collect(Collectors.toList());
	}

	@Override
	public Item findById(Long id, Integer cantidad) {
		// TODO Auto-generated method stub
		Producto producto = clienteFeign.detalle(id);
		return new Item(producto,cantidad);
	}
	
	@Autowired
	private IProductoClienteRest clienteFeign;

}
