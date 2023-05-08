package com.PayGoal.Postulacion.Utilities;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class RestHandler {

	/**
	 * Captura las excepciones lanzadas por el argumento de un método con la anotación @Valid y genera una respuesta HTTP.
	 * 
	 * @param e La excepción capturada.
	 * @return Una respuesta en formato JSON indicando los errores encontrados.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException e) {
		Map<String, String> jsonResponse = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			jsonResponse.put(fieldName, errorMessage);
		});
		return new ResponseEntity<>(jsonResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	/**
	 * Captura las excepciones lanzadas por una clase controladora con la anotación @Validated y genera una respuesta HTTP.
	 * 
	 * @param e La excepción capturada.
	 * @return Una respuesta en formato JSON indicando el error de la excepción. 
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraintValidationExceptions(ConstraintViolationException e) {
		Map<String, String> jsonResponse = new HashMap<>();
		e.getConstraintViolations().forEach(violation -> {
			String error = violation.getPropertyPath().toString();
			String message = violation.getMessage();
			jsonResponse.put(error, message);
		});
		return new ResponseEntity<>(jsonResponse, HttpStatus.UNPROCESSABLE_ENTITY);
	}

	/**
	 * Captura las excepciones del tipo ResponseStatusException y genera una respuesta HTTP con la información recibida.
	 * 
	 * @param e La excepción capturada.
	 * @return Una respuesta en formato JSON indicando el error de la excepción.
	 */
	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<?> handleStatusExceptions(ResponseStatusException e) {
		return handleMessageResponses(e.getReason(), HttpStatus.resolve(e.getStatusCode().value()));
	}

	
	/**
	 * Genera una respuesta HTTP con un mensaje.
	 * @param message El mensaje a enviar.
	 * @param status El código HTTP de la respuesta.
	 * @return Una respuesta en formato JSON con el mensaje recibido.
	 */
	public static ResponseEntity<?> handleMessageResponses(String message, HttpStatus status) {
		Map<String, String> jsonResponse = new HashMap<>();
		jsonResponse.put("message", message);
		return new ResponseEntity<>(jsonResponse, status);
	}

	
	/**
	 * Genera una respuesta con un elemento a una petición HTTP.
	 * @param data El elemento a enviar.
	 * @param status El código HTTP de la respuesta.
	 * @return Una respuesta en formato JSON con el elemento recibido.
	 */
	public static ResponseEntity<?> handleDataResponses(Object data, HttpStatus status) {
		return new ResponseEntity<>(data, status);
	}
}
