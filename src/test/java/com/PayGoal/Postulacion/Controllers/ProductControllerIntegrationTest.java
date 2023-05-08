package com.PayGoal.Postulacion.Controllers;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.PayGoal.Postulacion.Models.Product;
import com.PayGoal.Postulacion.Services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductController.class)
class ProductControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService service;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Prueba que el servidor envíe una lista de productos ordenados según su precio
	 * en orden ascendente en caso de que se encuentren al menos dos productos en la
	 * Base de Datos.
	 */
	@Test
	void testGetProductsPriceOrderedAsc() throws Exception {
		List<Product> products = new ArrayList<Product>();
		products.add(new Product(Long.valueOf(1), "producto A", "El producto N°1 de la base de datos",
				BigDecimal.valueOf(10), Long.valueOf(10)));
		products.add(new Product(Long.valueOf(2), "producto B", "El producto N°2 de la base de datos",
				BigDecimal.valueOf(5), Long.valueOf(250)));
		products.add(new Product(Long.valueOf(3), "producto C", "El producto N°3 de la base de datos",
				BigDecimal.valueOf(100), Long.valueOf(5)));

		products.forEach(prod -> {
			service.createProduct(prod);
		});
		Collections.sort(products, (p1, p2) -> {
			return p1.getPrecio().compareTo(p2.getPrecio());
		});
		Mockito.doReturn(products).when(service).getAllProductsOrderedByPrice(Direction.ASC);
		final String expectedResponseContent = objectMapper.writeValueAsString(products);
		this.mockMvc.perform(get("/api/productos?orden=ASC")).andExpect(status().isOk())
				.andExpect(content().json(expectedResponseContent));
		verify(service).getAllProductsOrderedByPrice(Direction.ASC);
	}

	/**
	 * Prueba que el servidor envíe una lista de productos ordenados según su precio
	 * en orden descendente en caso de que se encuentren al menos dos productos en
	 * la Base de Datos.
	 */
	@Test
	void testGetProductsPriceOrderedDesc() throws Exception {
		List<Product> products = new ArrayList<Product>();
		products.add(new Product(Long.valueOf(1), "producto A", "El producto N°1 de la base de datos",
				BigDecimal.valueOf(10), Long.valueOf(10)));
		products.add(new Product(Long.valueOf(2), "producto B", "El producto N°2 de la base de datos",
				BigDecimal.valueOf(5), Long.valueOf(250)));
		products.add(new Product(Long.valueOf(3), "producto C", "El producto N°3 de la base de datos",
				BigDecimal.valueOf(100), Long.valueOf(5)));

		products.forEach(prod -> {
			service.createProduct(prod);
		});
		Collections.sort(products, (p1, p2) -> {
			return p1.getPrecio().compareTo(p2.getPrecio());
		});
		Collections.reverse(products);
		Mockito.doReturn(products).when(service).getAllProductsOrderedByPrice(Direction.DESC);
		final String expectedResponseContent = objectMapper.writeValueAsString(products);
		this.mockMvc.perform(get("/api/productos?orden=DESC")).andExpect(status().isOk())
				.andExpect(content().json(expectedResponseContent));
		verify(service).getAllProductsOrderedByPrice(Direction.DESC);
	}

	/**
	 * Prueba que el servidor envíe un mensaje de error en caso de que no se
	 * encuentre ningún producto en la Base de Datos.
	 */
	@Test
	void testGetNoProductsPriceOrdered() throws Exception {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", "No se ha encontrado ningun producto en la base de datos");
		Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
				"No se ha encontrado ningun producto en la base de datos")).when(service)
				.getAllProductsOrderedByPrice(Direction.ASC);
		final String expectedResponseContent = objectMapper.writeValueAsString(message);
		this.mockMvc.perform(get("/api/productos?orden=ASC")).andExpect(status().isNotFound())
				.andExpect(content().json(expectedResponseContent));
		verify(service).getAllProductsOrderedByPrice(Direction.ASC);
	}

	/**
	 * Prueba que el servidor envíe el resultado esperado en caso de que se
	 * encuentre un producto con el id recibido como parámetro en la Base de Datos.
	 */
	@Test
	void testGetProductById() throws Exception {
		Product prod = new Product(Long.valueOf(1), "producto A", "El producto N°1 de la base de datos",
				BigDecimal.valueOf(10), Long.valueOf(10));
		service.createProduct(prod);
		Mockito.doReturn(prod).when(service).getProductById(Long.valueOf(1));
		final String expectedResponseContent = objectMapper.writeValueAsString(prod);
		this.mockMvc.perform(get("/api/productos/1")).andExpect(status().isOk())
				.andExpect(content().json(expectedResponseContent));
		verify(service).getProductById(Long.valueOf(1));
	}

	/**
	 * Prueba que el servidor envíe un mensaje de error en caso de que no se
	 * encuentre un producto con el id recibido como parámetro en la Base de Datos.
	 */
	@Test
	void testGetNoProductById() throws Exception {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", "No se ha encontrado un producto con el id: '1'");
		Mockito.doThrow(
				new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado un producto con el id: '1'"))
				.when(service).getProductById(Long.valueOf(1));
		final String expectedResponseContent = objectMapper.writeValueAsString(message);
		this.mockMvc.perform(get("/api/productos/1")).andExpect(status().isNotFound())
				.andExpect(content().json(expectedResponseContent));
		verify(service).getProductById(Long.valueOf(1));
	}

	/**
	 * Prueba que el servidor envíe el resultado esperado en caso de que se
	 * encuentre al menos un producto con el nombre recibido como parámetro en la
	 * Base de Datos.
	 */
	@Test
	void testGetProductsByName() throws Exception {
		List<Product> products = List.of(
				new Product(Long.valueOf(1), "producto con copias", "El producto N°1 de la base de datos",
						BigDecimal.valueOf(10), Long.valueOf(10)),
				new Product(Long.valueOf(2), "producto con copias", "El producto N°2 de la base de datos",
						BigDecimal.valueOf(5), Long.valueOf(250)));
		products.forEach(prod -> {
			service.createProduct(prod);
		});
		Mockito.doReturn(products).when(service).getProductsByName("producto con copias");
		final String expectedResponseContent = objectMapper.writeValueAsString(products);
		this.mockMvc.perform(get("/api/productos?nombre=producto con copias")).andExpect(status().isOk())
				.andExpect(content().json(expectedResponseContent));
		verify(service).getProductsByName("producto con copias");
	}

	/**
	 * Prueba que el servidor envíe un mensaje de error en caso de que no se
	 * encuentre ningún producto con el nombre recibido como parámetro en la Base de
	 * Datos.
	 */
	@Test
	void testGetNoProductsByName() throws Exception {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", "No se ha encontrado ningun producto con el nombre 'producto con copias'");
		Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
				"No se ha encontrado ningun producto con el nombre 'producto con copias'")).when(service)
				.getProductsByName("producto con copias");
		final String expectedResponseContent = objectMapper.writeValueAsString(message);
		this.mockMvc.perform(get("/api/productos?nombre=producto con copias")).andExpect(status().isNotFound())
				.andExpect(content().json(expectedResponseContent));
		verify(service).getProductsByName("producto con copias");
	}

	/**
	 * Prueba que el servidor envíe un mensaje de éxito en caso de que se intente
	 * cargar un producto valido a la Base de Datos.
	 */
	@Test
	void testCreateProduct() throws Exception {
		Product prod = new Product(null, "producto A", "El producto N°1 de la base de datos", BigDecimal.valueOf(10),
				Long.valueOf(10));
		Product resultingProd = new Product(Long.valueOf(1), "producto A", "El producto N°1 de la base de datos",
				BigDecimal.valueOf(10), Long.valueOf(10));
		Mockito.doReturn(resultingProd).when(service).createProduct(prod);
		final String expectedResponseContent = objectMapper.writeValueAsString(resultingProd);
		this.mockMvc
				.perform(post("/api/productos/").content(objectMapper.writeValueAsString(prod))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResponseContent));
		verify(service).createProduct(prod);
	}

	/**
	 * Prueba que el servidor envíe un mensaje de error en caso de que se intente
	 * cargar un producto invalido a la Base de Datos.
	 */
	@Test
	void testCreateInvalidProduct() throws Exception {
		Product prod = new Product();
		Map<String, String> errors = new HashMap<String, String>();
		errors.put("descripcion", "La descripcion no puede ser nulo ni debe estar vacio");
		errors.put("precio", "El precio no puede ser nulo");
		errors.put("cantidad", "La cantidad no puede ser nula");
		errors.put("nombre", "El nombre no puede ser nulo ni debe estar vacio");

		final String expectedResponseContent = objectMapper.writeValueAsString(errors);
		this.mockMvc
				.perform(post("/api/productos/").content(objectMapper.writeValueAsString(prod))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity()).andExpect(content().json(expectedResponseContent));
	}

	/**
	 * Prueba que el servidor envíe el resultado esperado en el caso de que se
	 * intente actualizar un producto existente en la Base de Datos.
	 */
	@Test
	void testUpdateProduct() throws Exception {
		Product prod = new Product(null, "producto A", "El producto N°1 de la base de datos", BigDecimal.valueOf(10),
				Long.valueOf(10));
		service.createProduct(prod);
		Product newProd = new Product(null, "producto A modificado", null, null, null);
		Product resultingProd = new Product(Long.valueOf(1), "producto A modificado",
				"El producto N°1 de la base de datos", BigDecimal.valueOf(10), Long.valueOf(10));
		Mockito.doReturn(resultingProd).when(service).updateProduct(Long.valueOf(1), newProd);
		final String expectedResponseContent = objectMapper.writeValueAsString(resultingProd);
		this.mockMvc
				.perform(patch("/api/productos/1").content(objectMapper.writeValueAsString(newProd))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().json(expectedResponseContent));
		verify(service).updateProduct(Long.valueOf(1), newProd);
	}

	/**
	 * Prueba que el servidor envíe un mensaje de error en caso de que se intente
	 * actualizar un producto con información invalida en la Base de Datos.
	 */
	@Test
	void testInvalidUpdateOnProduct() throws Exception {
		Product newProd = new Product(null, "producto A modificado", null, BigDecimal.valueOf(-1), Long.valueOf(-1));
		Map<String, String> errors = new HashMap<String, String>();
		errors.put("precio", "El precio no puede ser menor a $0");
		errors.put("cantidad", "La cantidad no puede ser menor a 1");
		final String expectedResponseContent = objectMapper.writeValueAsString(errors);
		this.mockMvc
				.perform(patch("/api/productos/1").content(objectMapper.writeValueAsString(newProd))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity()).andExpect(content().json(expectedResponseContent));
	}

	/**
	 * Prueba que el servidor envíe un mensaje de error en caso de que se intente
	 * actualizar un producto inexistente en la Base de Datos.
	 */
	@Test
	void testUpdateNoProduct() throws Exception {
		Product newProd = new Product(null, "producto A modificado", null, null, null);
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", "No se ha encontrado un producto con el id: '1'");
		Mockito.doThrow(
				new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado un producto con el id: '1'"))
				.when(service).updateProduct(Long.valueOf(1), newProd);
		final String expectedResponseContent = objectMapper.writeValueAsString(message);
		this.mockMvc
				.perform(patch("/api/productos/1").content(objectMapper.writeValueAsString(newProd))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound()).andExpect(content().json(expectedResponseContent));
		verify(service).updateProduct(Long.valueOf(1), newProd);
	}

	/**
	 * Prueba que el servidor envíe un mensaje de exito en caso de que se intente
	 * borrar un producto existente en la Base de Datos.
	 */
	@Test
	void testDeleteProduct() throws Exception {
		Product prod = new Product(null, "producto A", "El producto N°1 de la base de datos", BigDecimal.valueOf(10),
				Long.valueOf(10));
		service.createProduct(prod);
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", "El producto con el id '1' ha sido eliminado con exito");
		Mockito.doReturn(true).when(service).deleteProduct(Long.valueOf(1));
		final String expectedResponseContent = objectMapper.writeValueAsString(message);
		this.mockMvc.perform(delete("/api/productos/1")).andExpect(status().isOk())
				.andExpect(content().json(expectedResponseContent));
		verify(service).deleteProduct(Long.valueOf(1));
	}

	/**
	 * Prueba que el servidor envíe un mensaje de error en caso de que se intente
	 * borrar un producto inexistente en la Base de Datos.
	 */
	@Test
	void testDeleteNoProduct() throws Exception {
		Map<String, String> message = new HashMap<String, String>();
		message.put("message", "No existe un producto con el id: '1'");
		Mockito.doReturn(false).when(service).deleteProduct(Long.valueOf(1));
		final String expectedResponseContent = objectMapper.writeValueAsString(message);
		this.mockMvc.perform(delete("/api/productos/1")).andExpect(status().isConflict())
				.andExpect(content().json(expectedResponseContent));
		verify(service).deleteProduct(Long.valueOf(1));
	}

}
