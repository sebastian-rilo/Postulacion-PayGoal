package com.PayGoal.Postulacion.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PayGoal.Postulacion.Models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findByNombre(String nombre);
	
	Iterable<Product> findAllByNombre(String nombre);

}
