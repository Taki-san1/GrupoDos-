🐾 DogApp — Agenda de Citas para Mascotas

DogApp es una aplicación móvil nativa desarrollada en Kotlin, orientada a la administración y gestión de citas veterinarias. Su propósito es facilitar el registro, consulta, edición y eliminación de citas de mascotas mediante una interfaz intuitiva y almacenamiento local.

El proyecto forma parte del Miniproyecto 1 – Desarrollo de Aplicaciones para Dispositivos Móviles, de la Universidad del Valle.

---

🎯 Objetivo del proyecto

Desarrollar una aplicación móvil que permita al administrador de una veterinaria gestionar la información de sus pacientes y organizar sus citas de atención, integrando almacenamiento local, servicios REST y autenticación biométrica.

⚙️ Tecnologías y herramientas

Tecnología| Uso
Kotlin| Lenguaje de programación
Android SDK| Desarrollo de aplicaciones móviles
XML| Diseño de interfaces
Android Studio| Entorno de desarrollo
Code on the Go| Entorno de desarrollo complementario
Room / SQLite| Persistencia de datos local
Retrofit| Consumo de servicios REST
MVVM| Arquitectura de la aplicación
Repository| Gestión de acceso a datos
Git / GitHub| Control de versiones y colaboración
Navigation Component| Navegación entre pantallas
Fragments| Construcción de interfaces

📱 Historias de usuario

El proyecto contempla cinco módulos principales:

ID| Módulo| Descripción
HU 1.0| Login| Acceso mediante autenticación biométrica
HU 2.0| Administrador de citas| Visualización y gestión de citas registradas
HU 3.0| Nueva cita| Registro de mascotas y sus respectivos datos
HU 4.0| Detalle de cita| Consulta de información de una mascota
HU 5.0| Editar cita| Modificación de la información registrada

🏗️ Arquitectura

La aplicación seguirá el patrón arquitectónico MVVM (Model-View-ViewModel) complementado con el patrón Repository.

La organización del código estará orientada a la separación de responsabilidades:

- Data: acceso a fuentes de datos locales y remotas.
- Domain: modelos y lógica del negocio.
- UI: interfaces, Fragments y ViewModels.
- DI: configuración de dependencias.
- Utils: componentes y herramientas compartidas.

🌐 Servicios externos

DogApp utilizará los servicios de "Dog CEO API" (https://dog.ceo/dog-api/) para obtener información relacionada con las razas caninas y fotografías de mascotas.

Endpoints principales:

- Listado de razas: "https://dog.ceo/api/breeds/list/all"
- Imágenes aleatorias: "https://dog.ceo/api/breeds/image/random"

💾 Almacenamiento local

Se utilizará Room como capa de abstracción sobre SQLite para almacenar las citas veterinarias, permitiendo registrar, consultar, modificar y eliminar información sin depender de una conexión a Internet.

🤝 Colaboración y control de versiones

El desarrollo se realizará mediante Git y GitHub, utilizando ramas de trabajo para organizar las funcionalidades y facilitar la integración de los cambios realizados por los integrantes del equipo.

Se contemplan las siguientes ramas principales:

- "main": versión estable del proyecto.
- "develop": integración de funcionalidades.
- "feature/*": desarrollo de módulos específicos.

📋 Estado del proyecto

Versión: 0.1.0 — Desarrollo inicial.

El proyecto se encuentra en etapa de preparación y estructuración de su arquitectura base. Las funcionalidades serán implementadas progresivamente conforme al desarrollo de las historias de usuario.

🎓 Información académica

- Institución: Universidad del Valle.
- Asignatura: Desarrollo de Aplicaciones para Dispositivos Móviles.
- Proyecto: Miniproyecto 1.
- Nombre: DogApp — Agenda de Citas para Mascotas.
- Sprint: 1.

---

DogApp — Organización, tecnología y cuidado para nuestros pacientes de cuatro patas. 🐶
