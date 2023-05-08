package com.PayGoal.Postulacion.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.PayGoal.Postulacion.Models.Product;
import com.PayGoal.Postulacion.Services.ProductService;
import com.PayGoal.Postulacion.Utilities.RestHandler;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Validated
@RestController
@RequestMapping(value = "/api/productos", produces = "application/json")
public class ProductController {

	@Autowired
	ProductService productSv;

	/**
	 * Obtiene todos los productos en la Base de Datos ordenados según su precio.
	 * 
	 * @param order La dirección de ordenamiento.
	 * @return Una respuesta HTTP con todos los productos de la base de datos
	 *         ordenados según su precio.
	 */
	@GetMapping(params = "orden")
	public ResponseEntity<?> getProductsPriceOrdered(@RequestParam("orden") @NotBlank String order) {
		if (order.toUpperCase().equals("ASC") || order.toUpperCase().equals("DESC")) {
			return RestHandler.handleDataResponses(productSv.getAllProductsOrderedByPrice(Direction.fromString(order)),
					HttpStatus.OK);
		}
		return RestHandler.handleMessageResponses("El orden solicitado no existe", HttpStatus.UNPROCESSABLE_ENTITY);
	}

	/**
	 * Obtiene un producto de la Base de Datos que tenga el Id recibido por
	 * parámetro.
	 * 
	 * @param id El parámetro de búsqueda.
	 * @return Una respuesta HTTP con un producto del mismo Id.
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getProductById(@PathVariable("id") @NotNull Long id) {
		return RestHandler.handleDataResponses(productSv.getProductById(id), HttpStatus.OK);

	}

	/**
	 * Obtiene productos de la Base de Datos que tengan el nombre recibido por
	 * parámetro.
	 * 
	 * @param name El parámetro de búsqueda.
	 * @return Una respuesta HTTP con los productos del mismo nombre.
	 */
	@GetMapping(params = "nombre")
	public ResponseEntity<?> getProductsByName(@RequestParam("nombre") @NotBlank String name) {
		return RestHandler.handleDataResponses(productSv.getProductsByName(name), HttpStatus.OK);
	}

	/**
	 * Guarda un producto en la Base de Datos.
	 * 
	 * @param product El producto a guardar.
	 * @return Una copia de el producto cargado en la Base de Datos.
	 */
	@PostMapping("/")
	public ResponseEntity<?> createProduct(@RequestBody @Validated(Product.OnCreateValidation.class) Product product) {
		return RestHandler.handleDataResponses(productSv.createProduct(product), HttpStatus.OK);
	}

	/**
	 * Actualiza un producto que tenga el Id recibido por parámetro.
	 * 
	 * @param id      El parámetro de búsqueda.
	 * @param product un objeto con los nuevos valores de los parámetros a
	 *                actualizar.
	 * @return una copia del producto actualizado en la Base de Datos.
	 */
	@PatchMapping("/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable("id") @NotNull Long id,
			@RequestBody @Validated(Product.OnUpdateValidation.class) Product product) {
		return RestHandler.handleDataResponses(productSv.updateProduct(id, product), HttpStatus.OK);
	}

	/**
	 * Elimina un producto que tenga el Id recibido por parámetro.
	 * 
	 * @param id El parámetro de búsqueda.
	 * @return Una respuesta HTTP indicando el éxito o el fracaso de la operación
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable("id") @NotNull Long id) {
		boolean result = productSv.deleteProduct(id);
		if (!result) {
			return RestHandler.handleMessageResponses("No existe un producto con el id: '" + id + "'",
					HttpStatus.CONFLICT);
		}
		return RestHandler.handleMessageResponses("El producto con el id '" + id + "' ha sido eliminado con exito",
				HttpStatus.OK);
	}

}
