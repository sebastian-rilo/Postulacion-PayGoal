package com.PayGoal.Postulacion.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import org.springframework.data.annotation.ReadOnlyProperty;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
	@Id
	@GeneratedValue
	@ReadOnlyProperty
	private Long id;
	
	@NotBlank(message="El nombre no puede ser nulo ni debe estar vacio", groups = {OnCreateValidation.class})
	private String nombre;
	
	@NotBlank(message="La descripcion no puede ser nulo ni debe estar vacio", groups = {OnCreateValidation.class})
	private String descripcion;
	
	@Min(value = 0, message="El precio no puede ser menor a $0", groups = {OnCreateValidation.class, OnUpdateValidation.class})
	@NotNull(message="El precio no puede ser nulo", groups = {OnCreateValidation.class})
	private BigDecimal precio;
	
	@Min(value = 1, message="La cantidad no puede ser menor a 1", groups = {OnCreateValidation.class, OnUpdateValidation.class})
	@NotNull(message="La cantidad no puede ser nula", groups = {OnCreateValidation.class})
	private Long cantidad;
	
	
	/** 
	 * Método que reemplaza los parámetros modificables de un producto por los parámetros de otro producto
	 * @param newProduct El producto que va a facilitar los nuevos valores de los parámetros a modificar
	 */
	public void updateData(Product newProduct) {
		if(newProduct.nombre != null && !this.nombre.equals(newProduct.nombre)) {
			this.nombre = newProduct.nombre;
		}
		if(newProduct.descripcion != null && !this.descripcion.equals(newProduct.descripcion)) {
			this.descripcion = newProduct.descripcion;
		}
		if(newProduct.precio != null && !this.precio.equals(newProduct.precio)) {
			this.precio = newProduct.precio;
		}
		if(newProduct.cantidad != null && !this.cantidad.equals(newProduct.cantidad)) {
			this.cantidad = newProduct.cantidad;
		}
	}
	
	public interface OnCreateValidation {
		// interfaz para clasificar grupos de validaciones
	}
	
	public interface OnUpdateValidation {
		// interfaz para clasificar grupos de validaciones
	}
}
