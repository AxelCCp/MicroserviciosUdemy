package com.springboot.app.item.models;

public class Item {
	
	public Item() {
		
	}
	
	public Item(Producto producto, Integer cantidad) {
		this.producto = producto;
		this.cantidad = cantidad;
	}
	
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	
	public Double getTotal() {
		return cantidad.doubleValue() * producto.getPrecio();
	}
	
	private Producto producto;
	private Integer cantidad;
}
