package com.PayGoal.Postulacion.Models;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class ProductTest {

	@Test
	void testUpdateDataPartialUpdate() {
		Product productoViejo = new Product(Long.valueOf(1), "producto A", "El producto N°1", BigDecimal.valueOf(10),
				Long.valueOf(10));
		Product productoNuevo = new Product(null, "producto A Modificado", "El producto N°1, modificado",
				BigDecimal.valueOf(5), Long.valueOf(5));
		Product resultadoEsperado = new Product(Long.valueOf(1), "producto A Modificado", "El producto N°1, modificado",
				BigDecimal.valueOf(5), Long.valueOf(5));
		productoViejo.updateData(productoNuevo);
		assertEquals(resultadoEsperado, productoViejo);
	}

	@Test
	void testUpdateDataEmptyObject() {
		Product productoViejo = new Product(Long.valueOf(1), "producto A", "El producto N°1", BigDecimal.valueOf(10),
				Long.valueOf(10));
		Product productoNuevo = new Product();
		Product resultadoEsperado = new Product(Long.valueOf(1), "producto A", "El producto N°1", BigDecimal.valueOf(10),
				Long.valueOf(10));
		productoViejo.updateData(productoNuevo);
		assertEquals(resultadoEsperado, productoViejo);
	}

}
