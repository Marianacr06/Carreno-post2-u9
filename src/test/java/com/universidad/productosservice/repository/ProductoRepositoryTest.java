package com.universidad.productosservice.repository;

import com.universidad.productosservice.domain.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ProductoRepositoryTest {

    @Autowired
    private ProductoRepository productoRepository;

    @BeforeEach
    void setUp() {
        productoRepository.deleteAll();
    }

    @Test
    void save_asignaIdAutomaticamente() {
        Producto guardado = productoRepository
                .save(new Producto(null, "Laptop", 1500.0, 10));
        assertNotNull(guardado.getId());
        assertTrue(guardado.getId() > 0);
    }

    @Test
    void findById_existente_retornaProducto() {
        Producto guardado = productoRepository
                .save(new Producto(null, "Mouse", 50.0, 100));
        Optional<Producto> resultado =
                productoRepository.findById(guardado.getId());
        assertTrue(resultado.isPresent());
        assertEquals("Mouse", resultado.get().getNombre());
    }

    @Test
    void findAll_retornaListaCompleta() {
        productoRepository.save(new Producto(null, "Teclado", 80.0, 50));
        productoRepository.save(new Producto(null, "Monitor", 350.0, 20));
        List<Producto> productos = productoRepository.findAll();
        assertEquals(2, productos.size());
    }

    @Test
    void deleteById_eliminaProducto() {
        Producto guardado = productoRepository
                .save(new Producto(null, "Webcam", 90.0, 15));
        productoRepository.deleteById(guardado.getId());
        assertFalse(productoRepository.findById(guardado.getId()).isPresent());
    }

    @Test
    void save_conPrecioNegativo_debeGuardar() {
        Producto producto = new Producto(null, "Producto Descuento", -50.0, 5);
        Producto guardado = productoRepository.save(producto);
        assertNotNull(guardado.getId());
        assertEquals(-50.0, guardado.getPrecio());
    }

    @Test
    void findAll_baseDatosVacia_retornaListaVacia() {
        List<Producto> productos = productoRepository.findAll();
        assertEquals(0, productos.size());
    }

    @Test
    void update_modificaProductoExistente() {
        Producto guardado = productoRepository
                .save(new Producto(null, "Producto Original", 100.0, 10));
        
        guardado.setNombre("Producto Modificado");
        guardado.setPrecio(150.0);
        Producto actualizado = productoRepository.save(guardado);
        
        assertEquals("Producto Modificado", actualizado.getNombre());
        assertEquals(150.0, actualizado.getPrecio());
    }
}
