package com.universidad.productosservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.universidad.productosservice.domain.Producto;
import com.universidad.productosservice.service.ProductoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductoController.class)
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductoService productoService;

    @Test
    void listarProductos_retorna200ConLista() throws Exception {
        List<Producto> lista = List.of(
                new Producto(1L, "Laptop", 1500.0, 10),
                new Producto(2L, "Mouse", 50.0, 100));
        when(productoService.listarTodos()).thenReturn(lista);

        mockMvc.perform(get("/api/productos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Laptop"));
    }

    @Test
    void crearProducto_datosValidos_retorna201() throws Exception {
        Producto creado = new Producto(1L, "Tablet", 800.0, 5);
        when(productoService.crear(anyString(), anyDouble(), anyInt()))
                .thenReturn(creado);
        
        String json = """
                {"nombre":"Tablet","precio":800.0,"stock":5}""";
        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Tablet"));
    }

    @Test
    void buscarProducto_noExistente_retorna404() throws Exception {
        when(productoService.buscarPorId(99L))
                .thenThrow(new RuntimeException("Producto no encontrado: 99"));
        
        mockMvc.perform(get("/api/productos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void obtenerProducto_existente_retorna200() throws Exception {
        Producto producto = new Producto(1L, "Monitor", 350.0, 20);
        when(productoService.buscarPorId(1L))
                .thenReturn(producto);

        mockMvc.perform(get("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Monitor"));
    }

    @Test
    void actualizarProducto_datosValidos_retorna200() throws Exception {
        Producto actualizado = new Producto(1L, "Monitor Premium", 450.0, 15);
        when(productoService.actualizar(anyLong(), anyString(), anyDouble(), anyInt()))
                .thenReturn(actualizado);

        String json = """
                {"nombre":"Monitor Premium","precio":450.0,"stock":15}""";
        mockMvc.perform(put("/api/productos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Monitor Premium"));
    }

    @Test
    void eliminarProducto_existente_retorna204() throws Exception {
        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminarProducto_noExistente_retorna404() throws Exception {
        doThrow(new RuntimeException("Producto no encontrado: 99"))
                .when(productoService).eliminar(99L);

        mockMvc.perform(delete("/api/productos/99"))
                .andExpect(status().isNotFound());
    }
}
