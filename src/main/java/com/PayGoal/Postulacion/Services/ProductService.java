package com.PayGoal.Postulacion.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.PayGoal.Postulacion.Models.Product;
import com.PayGoal.Postulacion.Repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository productRepo;

	
	/**
	 * Obtiene todos los productos de la Base de datos. 
	 * @return Una Lista con todos los productos de la Base de Datos.
	 * @throws ResponseStatusException - En caso de que no se encuentre ningún producto.
	 */
	public List<Product> getAllProducts() throws ResponseStatusException {
		List<Product> products = new ArrayList<Product>();
		productRepo.findAll().forEach(products::add);
		if (products.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"No se ha encontrado ningun producto en la base de datos");
		}
		return products;
	}

	/**
	 * Obtiene todos los productos de la Base de datos ordenados según su precio.
	 * @param direction la dirección en la cual ordenar los productos.
	 * @return Una Lista con todos los productos ordenados de la Base de Datos.
	 * @throws ResponseStatusException - En caso de que no se encuentre ningún producto.
	 */
	public List<Product> getAllProductsOrderedByPrice(Direction direction) throws ResponseStatusException {
		List<Product> products = new ArrayList<Product>();
		productRepo.findAll(Sort.by(direction, "precio")).forEach(products::add);
		if (products.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"No se ha encontrado ningun producto en la base de datos");
		}
		return products;
	}

	/**
	 * Obtiene un producto de la Base de Datos por su Id.
	 * @param productId El parámetro de búsqueda.
	 * @return El producto obtenido.
	 * @throws ResponseStatusException - En caso de que no se encuentre un producto con ese Id.
	 */
	public Product getProductById(Long productId) throws ResponseStatusException {
		Optional<Product> prod = productRepo.findById(productId);
		if (prod.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"No se ha encontrado un producto con el id: '" + productId + "'");
		}
		return prod.get();
	}

	/**
	 * Obtiene una lista de productos de la Base de Datos por su nombre.
	 * @param productName El parámetro de búsqueda.
	 * @return El producto obtenido.
	 * @throws ResponseStatusException - En caso de que no se encuentre ningún producto con ese nombre.
	 */
	public List<Product> getProductsByName(String productName) throws ResponseStatusException{
		List<Product> products = new ArrayList<Product>();
		productRepo.findAllByNombre(productName).forEach(products::add);
		if (products.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"No se ha encontrado ningun producto con el nombre '"+productName+"'");
		}
		return products;
	}

	/**
	 * Guarda un producto en la Base de Datos.
	 * @param prod El producto a Guardar.
	 * @return Una copia del producto guardado.
	 */
	public Product createProduct(Product prod) {
		return productRepo.save(prod);
	}

	/**
	 * Busca y actualiza un producto en la Base de Datos por su Id.
	 * @param productId El parámetro de búsqueda.
	 * @param newProd un objeto con los nuevos valores de los parámetros a actualizar.
	 * @return Una copia del producto actualizado.
	 * @throws ResponseStatusException - En caso de que no se encuentre un producto con ese Id.
	 */
	public Product updateProduct(Long productId, Product newProd) throws ResponseStatusException {
		Product oldProd = getProductById(productId);
		oldProd.updateData(newProd);
		return productRepo.save(oldProd);

	}

	/**
	 * Busca y elimina un producto por su Id.
	 * @param productId El parámetro de búsqueda.
	 * @return true si se encuentra y elimina el producto. false si no hay ninguno con ese Id en la Base de Datos. 
	 */
	public boolean deleteProduct(Long productId) {
		if (productRepo.findById(productId).isEmpty()) {
			return false;
		}
		productRepo.deleteById(productId);
		return true;
	}
}
