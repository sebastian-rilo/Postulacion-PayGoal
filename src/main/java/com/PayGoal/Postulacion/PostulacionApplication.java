package com.PayGoal.Postulacion;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.PayGoal.Postulacion.Models.Product;
import com.PayGoal.Postulacion.Services.ProductService;

@SpringBootApplication
public class PostulacionApplication {

	@Autowired
	ProductService productSv;

	public static void main(String[] args) {
		SpringApplication.run(PostulacionApplication.class, args);
	}

	/**
	 * Método que carga productos al momento de inicializar el servidor.
	 */
	@Bean
	CommandLineRunner runner() {
		return args -> {
			List<Product> prods = List.of(
					new Product(Long.valueOf(1), "producto A", "El producto N°1 de la base de datos",
							BigDecimal.valueOf(10), Long.valueOf(10)),
					new Product(Long.valueOf(2), "producto B", "El producto N°2 de la base de datos",
							BigDecimal.valueOf(5), Long.valueOf(250)),
					new Product(Long.valueOf(3), "producto C", "El producto N°3 de la base de datos",
							BigDecimal.valueOf(100), Long.valueOf(5)),
					new Product(Long.valueOf(4), "producto A", "El producto N°4 de la base de datos",
							BigDecimal.valueOf(200), Long.valueOf(50)));
			prods.forEach(prod -> {
				productSv.createProduct(prod);
			});
		};
	}

}
