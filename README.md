# 📋 API REST de Gestión de Tareas

API REST desarrollada con **Java y Spring Boot** para la gestión de tareas. Este proyecto personal forma parte de mi portfolio de desarrollo backend y está orientado a poner en práctica el diseño de APIs, la seguridad, la persistencia de datos y las pruebas automatizadas.

## 🚀 Tecnologías

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security**
- **JSON Web Tokens (JWT)**
- **MySQL**
- **Maven**
- **Docker y Docker Compose**
- **Swagger / OpenAPI**
- **JUnit 5 y Mockito**
- **Git y GitHub**

## ✨ Funcionalidades

### Gestión de tareas
- Operaciones CRUD para crear, consultar, actualizar y eliminar tareas.
- Validación de datos mediante Bean Validation.
- Uso de DTOs para estructurar los datos de entrada y salida.
- Manejo global de excepciones para centralizar el tratamiento de errores.
- Persistencia de datos mediante Spring Data JPA y MySQL.

### Búsqueda y consulta
- Filtrado de tareas por texto y estado de finalización.
- Consulta de tareas completadas y pendientes.
- Ordenación de resultados.
- Paginación para controlar el volumen de datos devuelto por las consultas.

### Seguridad
- Autenticación mediante JWT.
- Registro e inicio de sesión de usuarios.
- Protección de los endpoints que requieren autenticación mediante Spring Security.
- Almacenamiento seguro de contraseñas mediante BCrypt.

### Calidad y despliegue
- Pruebas automatizadas con JUnit 5 y Mockito.
- Pruebas de integración para comprobar el comportamiento de la API.
- Documentación interactiva mediante Swagger / OpenAPI.
- Contenerización de la aplicación y de la base de datos con Docker Compose.

## ⚙️ Requisitos previos

Para ejecutar el proyecto necesitas:

- Docker Desktop con Docker Compose.
- Git, si quieres clonar el repositorio.
- Java 17 si deseas ejecutar o desarrollar la aplicación fuera de Docker.

## ▶️ Instalación y ejecución

Clona el repositorio:

```bash
git clone https://github.com/javierbragulatm/tareas-api-springboot.git
cd tareas-api-springboot
```

Inicia la aplicación y sus servicios mediante Docker Compose:

```bash
docker compose up --build
```

Una vez iniciados los servicios, la API estará disponible en:

```text
http://localhost:8080
```

Para detener los contenedores:

```bash
docker compose down
```

## 📖 Documentación de la API

Con la aplicación en ejecución, puedes consultar la documentación interactiva de Swagger y probar los endpoints desde el navegador:

**[Abrir Swagger UI](http://localhost:8080/swagger-ui/index.html)**

La especificación OpenAPI está disponible en:

```text
http://localhost:8080/v3/api-docs
```

## 🔐 Autenticación

La API utiliza JWT para autenticar las solicitudes a los endpoints protegidos.

1. Registra un usuario mediante el endpoint `/register`.
2. Inicia sesión mediante `/login` para obtener el token JWT.
3. Incluye el token en las solicitudes protegidas mediante la cabecera `Authorization`:

```http
Authorization: Bearer <token>
```

Los endpoints públicos permiten acceder al registro, al inicio de sesión y a la documentación, mientras que el resto de las rutas requieren autenticación.

## 🧪 Ejecutar las pruebas

En Windows, puedes ejecutar la suite de pruebas con el Maven Wrapper incluido en el proyecto:

```powershell
.\mvnw.cmd test
```

Las pruebas permiten verificar el comportamiento de la aplicación y detectar regresiones durante el desarrollo.

## 🛠️ Objetivos del proyecto

Este proyecto me permite profundizar en:

- El desarrollo de servicios REST con Java y Spring Boot.
- La arquitectura de aplicaciones backend y la separación de responsabilidades.
- La persistencia y consulta de datos con Spring Data JPA y MySQL.
- La autenticación y autorización mediante Spring Security y JWT.
- La validación de datos y el tratamiento de excepciones.
- La automatización de pruebas.
- La ejecución de aplicaciones mediante contenedores Docker.

## 👨‍💻 Autor

**Javier Bragulat**

Ingeniero Informático orientado al desarrollo backend, con interés en Java, Spring Boot, APIs REST y desarrollo de aplicaciones.

GitHub: [@javierbragulatm](https://github.com/javierbragulatm)

