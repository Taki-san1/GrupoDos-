
# DogApp 🐶

Aplicación móvil para la gestión de citas veterinarias de mascotas.

## Información del proyecto

- **Institución:** Universidad del Valle - Sede Yumbo
- **Asignatura:** Desarrollo de Aplicaciones para Dispositivos Móviles
- **Proyecto:** Miniproyecto 1
- **Nombre:** DogApp
- **Versión:** 1.0

## Descripción

DogApp es una aplicación Android diseñada para registrar, consultar,
editar y eliminar citas de mascotas.

Su propósito es facilitar la administración de la información de
cada paciente y de sus propietarios mediante una interfaz sencilla.

## Funcionalidades principales

- Inicio de sesión mediante autenticación biométrica.
- Visualización de las citas registradas.
- Registro de nuevas citas veterinarias.
- Consulta detallada de cada mascota.
- Edición de la información de las citas.
- Eliminación de citas registradas.
- Consulta de razas e imágenes mediante Dog CEO API.
- Almacenamiento local de información mediante Room.

## Tecnologías

- Kotlin
- Android Studio
- Android SDK
- XML
- Arquitectura MVVM
- Repository Pattern
- Room
- Retrofit
- Navigation Component
- RecyclerView
- Lottie
- Glide
- Dog CEO API

## Arquitectura

El proyecto utiliza una arquitectura MVVM con Repository,
separando la interfaz de usuario, la lógica de presentación
y el acceso a los datos.

## Requisitos de desarrollo

- Android Studio.
- JDK 17.
- Android SDK API 36.
- Gradle 9.5.
- Android Gradle Plugin 9.3.1.
- Kotlin 2.4.21.

## Estructura general

```text
GrupoDos/
├── app/
│   ├── src/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
├── build.gradle.kts
├── gradle.properties
├── gradlew
├── gradlew.bat
├── local.properties.example
├── settings.gradle.kts
└── README.md
```

## API externa

Dog CEO API:
https://dog.ceo/dog-api/

Se utiliza para consultar razas e imágenes de perros.

## Equipo de desarrollo

Universidad del Valle - Sede Yumbo.

**Integrantes:**  
- Completar con los integrantes del equipo.

## Estado del proyecto

En desarrollo.
