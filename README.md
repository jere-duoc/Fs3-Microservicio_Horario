# Microservicio de Horarios - Hospital DuocQuin 📅

Gestiona la planificación de turnos, jornadas laborales y el registro de horas extra para todo el personal clínico.

## 🛠️ Tecnologías
- **Java 17**
- **Spring Boot 3.x**
- **MySQL**
- **Maven**

## 📋 Funcionalidades
- **Gestión de Turnos**: Configuración de horarios Diurnos, Vespertinos y Nocturnos.
- **Asignación de Horarios**: Registro de jornadas vinculadas a usuarios y salas específicas.
- **Generación Masiva**: Algoritmo para la creación automática de calendarios laborales mensuales.
- **Horas Extra**: Cálculo y registro de tiempo adicional trabajado para integración con nómina.

## ⚙️ Configuración y Ejecución
1. Configurar la base de datos MySQL usando el esquema incluido.
2. Actualizar `src/main/resources/application.properties`.
3. Ejecutar el servicio:
```bash
./mvnw spring-boot:run
```
El servicio estará disponible en `http://localhost:8082`.

## 📡 API Endpoints Principales
- `GET /api/horarios`: Listar todos los horarios.
- `GET /api/horarios/usuario/{id}`: Consultar horario de un funcionario específico.
- `POST /api/horarios/generar/aleatorio`: Generar turnos automáticos.
- `POST /api/horarios/generar/por-turno`: Generar turnos basados en una plantilla específica.
