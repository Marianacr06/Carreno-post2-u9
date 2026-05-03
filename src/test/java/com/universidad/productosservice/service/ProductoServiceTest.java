package com.universidad.productosservice.service;

import com.universidad.productosservice.domain.Producto;
import com.universidad.productosservice.repository.ProductoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        producto = new Producto(1L, "Laptop", 1500.0, 10);
    }

    @Test
    void listarTodos_retornaListaDelRepositorio() {
        List<Producto> productos = List.of(producto);
        when(productoRepository.findAll()).thenReturn(productos);

        List<Producto> resultado = productoService.listarTodos();

        assertEquals(1, resultado.size());
        assertEquals("Laptop", resultado.get(0).getNombre());
        verify(productoRepository).findAll();
    }

    @Test
    void buscarPorId_existente_retornaProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));

        Producto resultado = productoService.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
        assertEquals("Laptop", resultado.getNombre());
        verify(productoRepository).findById(1L);
    }

    @Test
    void buscarPorId_inexistente_lanzaExcepcion() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productoService.buscarPorId(99L));

        assertEquals("Producto no encontrado: 99", exception.getMessage());
        verify(productoRepository).findById(99L);
    }

    @Test
    void crear_guardaYRetornaProducto() {
        Producto guardado = new Producto(2L, "Mouse", 50.0, 100);
        when(productoRepository.save(any(Producto.class))).thenReturn(guardado);

        Producto resultado = productoService.crear("Mouse", 50.0, 100);

        assertEquals(2L, resultado.getId());
        assertEquals("Mouse", resultado.getNombre());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void actualizar_existente_modificaYRetornaProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Producto resultado = productoService.actualizar(1L, "Laptop Pro", 1800.0, 8);

        assertEquals(1L, resultado.getId());
        assertEquals("Laptop Pro", resultado.getNombre());
        assertEquals(1800.0, resultado.getPrecio());
        assertEquals(8, resultado.getStock());
        verify(productoRepository).findById(1L);
        verify(productoRepository).save(producto);
    }

    @Test
    void eliminar_existente_eliminaProducto() {
        when(productoRepository.existsById(1L)).thenReturn(true);

        productoService.eliminar(1L);

        verify(productoRepository).existsById(1L);
        verify(productoRepository).deleteById(1L);
    }

    @Test
    void eliminar_inexistente_lanzaExcepcionYNoElimina() {
        when(productoRepository.existsById(99L)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> productoService.eliminar(99L));

        assertEquals("Producto no encontrado: 99", exception.getMessage());
        verify(productoRepository).existsById(99L);
        verify(productoRepository, never()).deleteById(99L);
    }
}