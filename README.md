# Productos Service

![CI](https://github.com/marianacarrenorangel/Carreno-post2-9/actions/workflows/ci.yml/badge.svg)

## Cobertura

![Reporte JaCoCo](img/CAPTURA%20.png)

## Descripción

Microservicio de gestión de productos con suite completa de pruebas unitarias e integración para la capa de persistencia (JPA) y la capa web (REST). Implementa un pipeline de CI/CD automatizado con GitHub Actions y genera reportes de cobertura con JaCoCo.

## Requisitos Previos

- JDK 21
- Maven 3.9+
- Git configurado (nombre de usuario y email)
- Cuenta en GitHub

## Estructura del Proyecto

```
productos-service/
├── src/
│   ├── main/
│   │   ├── java/com/universidad/productosservice/
│   │   │   ├── ProductosServiceApplication.java
│   │   │   ├── domain/
│   │   │   │   └── Producto.java
│   │   │   ├── repository/
│   │   │   │   └── ProductoRepository.java
│   │   │   ├── service/
│   │   │   │   └── ProductoService.java
│   │   │   └── controller/
│   │   │       └── ProductoController.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/universidad/productosservice/
│       │   ├── repository/
│       │   │   └── ProductoRepositoryTest.java
│       │   └── controller/
│       │       └── ProductoControllerTest.java
│       └── resources/
│           └── application-test.properties
├── .github/
│   └── workflows/
│       └── ci.yml
├── pom.xml
└── README.md
```

