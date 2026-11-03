# 📋 API REST de Gestión de Tareas

API REST desarrollada con **Java 17 y Spring Boot** para la gestión de tareas y usuarios.

Este proyecto personal forma parte de mi portfolio de desarrollo backend y está orientado a poner en práctica el diseño de APIs REST, la seguridad, la persistencia de datos, la mensajería asíncrona y las pruebas automatizadas.

## 🚀 Tecnologías

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security**
- **JWT (JSON Web Tokens)**
- **BCrypt**
- **MySQL**
- **Apache Kafka**
- **Flyway**
- **Maven**
- **Docker y Docker Compose**
- **Swagger / OpenAPI**
- **Spring Boot Actuator**
- **JUnit 5 y Mockito**
- **Git y GitHub Actions**

## ✨ Funcionalidades

### Gestión de tareas

- Operaciones CRUD para crear, consultar, actualizar y eliminar tareas.
- Validación de datos mediante Bean Validation.
- Uso de DTOs para estructurar los datos de entrada y salida.
- Gestión global de excepciones para centralizar el tratamiento de errores.
- Persistencia de datos mediante Spring Data JPA y MySQL.

### Búsqueda y consulta

- Búsqueda de tareas por texto.
- Filtrado por texto y estado de finalización.
- Consulta de tareas completadas y pendientes.
- Ordenación de resultados.
- Paginación para controlar el volumen de datos devuelto por las consultas.

### Seguridad

- Registro e inicio de sesión de usuarios.
- Autenticación mediante JWT.
- Protección de endpoints mediante Spring Security.
- Almacenamiento seguro de contraseñas mediante BCrypt.
- Aislamiento de datos entre usuarios para impedir el acceso a tareas de otras cuentas.

### Mensajería asíncrona

- Integración con Apache Kafka.
- Publicación de eventos al crear tareas.
- Consumidor para procesar los eventos publicados.
- Configuración que permite desactivar Kafka en los entornos de prueba.

### Calidad y mantenimiento

- Pruebas unitarias y de integración con JUnit 5 y Mockito.
- Base de datos H2 para las pruebas.
- Migraciones de esquema de base de datos mediante Flyway.
- Documentación interactiva mediante Swagger / OpenAPI.
- Monitorización mediante Spring Boot Actuator.
- Integración continua con GitHub Actions.

## 🏗️ Arquitectura

El proyecto sigue una arquitectura por capas, separando las responsabilidades de cada componente:

- **Controller:** recibe las peticiones HTTP y gestiona las respuestas.
- **Service:** contiene la lógica de negocio.
- **Repository:** gestiona el acceso a los datos mediante Spring Data JPA.
- **DTO:** define los objetos utilizados para intercambiar información con la API.
- **Security:** gestiona la autenticación y la protección de los endpoints.
- **Messaging:** gestiona la publicación y el consumo de eventos mediante Kafka.

## ⚙️ Requisitos previos

Para ejecutar el proyecto necesitas:

- Git.
- Docker Desktop con Docker Compose.
- Java 17 si deseas compilar o ejecutar la aplicación fuera de Docker.

## ▶️ Instalación y ejecución

Clona el repositorio:

```bash
git clone https://github.com/javierbragulatm/tareas-api-springboot.git
cd tareas-api-springboot
```

### 1. Configuración

Crea un archivo `.env` en la raíz del proyecto con las variables necesarias para la configuración de Docker Compose:

```dotenv
DB_PASSWORD=tu_password_mysql
JWT_SECRET=tu_clave_secreta_jwt_de_al_menos_32_caracteres
```

Sustituye los valores de ejemplo por tus propios valores. No subas el archivo `.env` a GitHub.

### 2. Compilación

Antes de iniciar Docker Compose, genera el JAR de la aplicación:

```powershell
.\mvnw.cmd clean package -DskipTests
```

### 3. Inicio de los servicios

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

Para detenerlos y eliminar también los volúmenes de datos:

```bash
docker compose down -v
```

**Nota:** este último comando elimina los datos persistidos en los volúmenes.

## 📖 Documentación de la API

Con la aplicación en ejecución, puedes consultar la documentación interactiva de Swagger y probar los endpoints desde el navegador:

[**Abrir Swagger UI**](http://localhost:8080/swagger-ui/index.html)

La especificación OpenAPI está disponible en:

```text
http://localhost:8080/v3/api-docs
```

## 🔐 Autenticación

La API utiliza JWT para autenticar las solicitudes a los endpoints protegidos.

1. Registra un usuario mediante `POST /register`.
2. Inicia sesión mediante `POST /login` para obtener un token JWT.
3. Incluye el token en las solicitudes protegidas mediante la cabecera `Authorization`:

```http
Authorization: Bearer <token>
```

Los endpoints de registro, inicio de sesión y documentación son públicos. El resto de las rutas requieren autenticación.

## 🧪 Ejecutar las pruebas

El proyecto incluye pruebas unitarias y de integración para comprobar el comportamiento de sus componentes y detectar posibles regresiones.

En Windows, puedes ejecutar todas las verificaciones de Maven mediante:

```powershell
.\mvnw.cmd clean verify
```

Las pruebas utilizan H2 como base de datos de pruebas y permiten comprobar el funcionamiento de la aplicación sin depender de una instancia externa de MySQL.

## 🔄 Integración continua

El repositorio incorpora un workflow de GitHub Actions para automatizar las comprobaciones del proyecto.

Este workflow ejecuta las verificaciones de Maven y comprueba la construcción de la imagen Docker.

## 🛠️ Objetivos del proyecto

Este proyecto me permite profundizar en:

- El desarrollo de servicios REST con Java y Spring Boot.
- La arquitectura de aplicaciones backend y la separación de responsabilidades.
- La persistencia y consulta de datos con Spring Data JPA y MySQL.
- La autenticación y autorización mediante Spring Security y JWT.
- La integración de mensajería asíncrona con Apache Kafka.
- La gestión de esquemas de bases de datos mediante Flyway.
- La validación de datos y el tratamiento de excepciones.
- La automatización de pruebas.
- La ejecución de aplicaciones mediante contenedores Docker.

## 👨‍💻 Autor

**Javier Bragulat Martínez**

Ingeniero Informático orientado al desarrollo backend, con interés en Java, Spring Boot y el desarrollo de aplicaciones.

GitHub: [@javierbragulatm](https://github.com/javierbragulatm)
