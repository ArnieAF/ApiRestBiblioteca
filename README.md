# 📚 API Biblioteca

API REST desarrollada en **Java 17 + Spring Boot 3.3.5** para la gestión de una biblioteca: autores, libros, usuarios y reservas. Proyecto desarrollado como práctica en Platzi.

---

## 🧱 Tecnologías

| Tecnología | Versión / Detalle |
|---|---|
| Java | 17 |
| Spring Boot | 3.3.5 |
| Spring Data JPA / Hibernate | 6.5.3.Final |
| Base de datos | Microsoft SQL Server 2022 |
| Driver JDBC | mssql-jdbc 12.6.4 |
| Documentación API | springdoc-openapi (Swagger UI) 2.0.2 |
| Mapeo DTO ↔ Entidad | MapStruct 1.5.5 |
| Testing | JUnit 5, Mockito, Hamcrest |
| Build | Maven |

---

## 🏗️ Arquitectura

El proyecto sigue una arquitectura en capas con separación entre **dominio** y **persistencia**, conectadas mediante mappers:

```
com.api.api_biblioteca
├── controller/          → Controladores REST (Author, Book, Reserve, User)
├── domain/
│   ├── dto/             → Objetos de transferencia de datos
│   ├── repository/      → Interfaces de repositorio de dominio
│   ├── service/         → Lógica de negocio
│   └── (entidades)      → Author, Book, Genre, Reservation, User
├── exception/           → Manejo centralizado de errores
│   ├── GlobalExceptionHandler
│   ├── ResourceNotFoundException
│   └── UnauthorizedAccessException
└── persistence/
    ├── crud/            → Interfaces CrudRepository (Spring Data JPA)
    ├── entity/          → Entidades JPA (Autor, Libro, Reserva, Usuario, Genero)
    ├── mapper/          → Conversores entidad de persistencia ↔ modelo de dominio
    └── repository/      → Implementaciones de los repositorios de dominio
```

> **Nota de diseño:** el dominio está modelado en inglés (`Author`, `Book`, `Reservation`, `User`) mientras que la capa de persistencia usa nombres en español (`Autor`, `Libro`, `Reserva`, `Usuario`), conectados por la capa `mapper`. Es un patrón de separación intencional entre el modelo de negocio y el modelo de base de datos.

---

## ⚙️ Requisitos previos

- **JDK 17**
- **Maven** (o el wrapper incluido `mvnw` / `mvnw.cmd`)
- **Docker Desktop** (para levantar SQL Server fácilmente) — o una instancia de SQL Server ya instalada

---

## 🐳 Levantar la base de datos con Docker

```bash
docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=Passw0rd2026!" -p 1433:1433 --name sql-biblioteca -d mcr.microsoft.com/mssql/server:2022-latest
```

Verificar que el contenedor esté corriendo:

```bash
docker ps
```

### Crear la base de datos y el usuario de la aplicación

Conectate al contenedor con `sqlcmd`:

```bash
docker exec -it sql-biblioteca /opt/mssql-tools18/bin/sqlcmd -S localhost -U sa -P "Passw0rd2026!" -C
```

Ejecutá los siguientes bloques **uno por uno**, presionando `GO` después de cada uno:

```sql
CREATE DATABASE Biblioteca;
GO
```
```sql
USE Biblioteca;
GO
```
```sql
CREATE LOGIN Api WITH PASSWORD = 'ArnieXDXDXD2026!';
GO
```
```sql
CREATE USER Api FOR LOGIN Api;
GO
```
```sql
ALTER ROLE db_owner ADD MEMBER Api;
GO
```
```sql
EXIT
```

> ⚠️ SQL Server exige contraseñas de al menos 8 caracteres que incluyan 3 de estas 4 categorías: mayúsculas, minúsculas, números y símbolos.

---

## 🔧 Configuración (`application.properties`)

```properties
server.port=8080
spring.profiles.active=dev
server.servlet.context-path=/arni/api-biblioteca
springdoc.swagger-ui.path=/swagger-ui.html

# Conexión a SQL Server
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=Biblioteca;encrypt=false
spring.datasource.username=Api
spring.datasource.password=ArnieXDXDXD2026!
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# Hibernate
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.format_sql=true

# Carga de datos de prueba (data.sql)
spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always

# Estrategia de nombres (no modifica nombres de columnas)
spring.jpa.hibernate.naming.physical-strategy=org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
spring.jpa.hibernate.naming.implicit-strategy=org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl
```

### Sobre `ddl-auto`

| Valor | Comportamiento |
|---|---|
| `create-drop` | Borra y recrea las tablas en cada arranque. Ideal para desarrollo/pruebas, ya que combinado con `data.sql` siempre arranca con datos limpios y consistentes. |
| `update` | Conserva los datos entre reinicios, solo actualiza el esquema si cambian las entidades. Recomendado cuando ya no querés perder datos cargados manualmente. |

---

## 🌱 Datos de prueba (`data.sql`)

El archivo `src/main/resources/data.sql` se ejecuta automáticamente al arrancar (gracias a `spring.sql.init.mode=always` + `spring.jpa.defer-datasource-initialization=true`) e inserta:

- 5 autores (García Márquez, Allende, Borges, Vargas Llosa, Rowling)
- 3 usuarios
- 7 libros distribuidos entre los autores, con distintos géneros y disponibilidad
- 4 reservas de ejemplo

> Si usás `ddl-auto=create-drop`, los IDs autogenerados siempre serán consistentes con el orden del script. Si usás `update`, evitá reiniciar la app repetidamente sin limpiar `data.sql`, ya que se insertarán registros duplicados en cada arranque.

---

## ▶️ Cómo ejecutar el proyecto

1. Levantar el contenedor de SQL Server (ver sección Docker).
2. Verificar que `application.properties` tenga los datos de conexión correctos.
3. Ejecutar la clase principal `ApiBibliotecaApplication` desde tu IDE, o por terminal:

```bash
./mvnw spring-boot:run
```

4. Confirmar en la consola que la aplicación arrancó correctamente:

```
Tomcat started on port 8080 (http) with context path '/arni/api-biblioteca'
Started ApiBibliotecaApplication in X seconds
```

---

## 📖 Documentación interactiva (Swagger UI)

Una vez la app esté corriendo, accedé a:

```
http://localhost:8080/arni/api-biblioteca/swagger-ui.html
```

Ahí vas a encontrar el listado completo y actualizado de todos los endpoints disponibles, agrupados por controlador (`author-controller`, `book-controller`, `reserve-controller`, `user-controller`), junto con la posibilidad de probarlos directamente desde el navegador con el botón **"Try it out"**.

---

## 🔌 Endpoints principales

> Todas las rutas están precedidas por el context path: `http://localhost:8080/arni/api-biblioteca`

### Authors (`/authors`)
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/authors/all` | Listar todos los autores |
| GET | `/authors/allNameAsc` | Listar autores ordenados por nombre (ascendente) |
| GET | `/authors/allNameDesc` | Listar autores ordenados por nombre (descendente) |
| GET | `/authors/exact/{name}` | Buscar autor por nombre exacto |
| GET | `/authors/contains/{name}` | Buscar autores cuyo nombre contenga un texto |
| GET | `/authors/{nationality}` | Buscar autores por nacionalidad |
| GET | `/authors/count/{nationality}` | Contar autores por nacionalidad |
| POST | `/authors/save` | Crear un nuevo autor |
| DELETE | `/authors/delete/{name}` | Eliminar un autor por nombre |

### Books (`/books`)
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/books/findByTitle/{title}` | Buscar libro por título exacto |
| GET | `/books/author/{authorId}` | Listar libros de un autor |
| GET | `/books/genre/{genre}` | Listar libros por género |
| GET | `/books/available` | Listar libros disponibles |

### Reservations (`/reservations`)
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/reservations/save` | Crear una nueva reserva |
| GET | `/reservations/user/{userId}` | Listar reservas de un usuario |
| GET | `/reservations/user/{userId}/book/{bookId}` | Buscar reserva por usuario y libro |
| GET | `/reservations/after/{date}` | Listar reservas hechas después de una fecha |
| GET | `/reservations/exists/{reservationId}/before/{date}` | Verificar si una reserva expira antes de una fecha |

### Users (`/users`)
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/users/save` | Crear un nuevo usuario |
| GET | `/users/{userId}` | Buscar usuario por ID |
| GET | `/users/email/{email}` | Buscar usuario por email |
| GET | `/users/exists/{email}` | Verificar si existe un usuario con ese email |
| GET | `/users/contains/{name}` | Buscar usuarios cuyo nombre contenga un texto |
| GET | `/users/registered-between` | Listar usuarios registrados entre dos fechas |
| GET | `/users/count-registered-between` | Contar usuarios registrados entre dos fechas |
| DELETE | `/users/delete/{userId}` | Eliminar usuario por ID |

> Para el listado exacto y siempre actualizado, consultar Swagger UI.

---

## 🧪 Probar la API

### Con Postman
Importar las rutas de la tabla anterior usando como base:
```
http://localhost:8080/arni/api-biblioteca
```

### Con Swagger UI
Recomendado para pruebas rápidas: permite ejecutar cualquier endpoint sin salir del navegador.

---

## 🛠️ Manejo de errores

El proyecto centraliza el manejo de excepciones en `GlobalExceptionHandler`, devolviendo respuestas HTTP consistentes:

- `404 Not Found` → `ResourceNotFoundException` (recurso no encontrado)
- `401/403` → `UnauthorizedAccessException` (acceso no autorizado)

---

## 📌 Notas y consideraciones

- El estilo de las rutas de búsqueda (ej. `/authors/contains/{name}`, `/authors/exact/{name}`) sigue un patrón más cercano a RPC-sobre-HTTP que a REST orientado a recursos puro. Es funcional y está bien documentado con Swagger, pero no implementa HATEOAS ni sigue estrictamente las convenciones de nomenclatura RESTful (filtros como query params en vez de sub-rutas).
- El proyecto no incluye autenticación/autorización activa más allá de la excepción `UnauthorizedAccessException` definida; no hay Spring Security configurado.
- `spring.jpa.open-in-view` está habilitado por defecto (comportamiento estándar de Spring Boot), lo cual puede generar consultas adicionales durante el renderizado de la vista/respuesta. Considerar deshabilitarlo explícitamente en producción.

---
