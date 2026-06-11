# Contenedores UI - Supabase

Aplicación móvil desarrollada en Android utilizando Kotlin, que permite consultar y visualizar información de estudiantes almacenada en Supabase. Los datos se presentan en un `ListView` con un diseño personalizado que facilita la visualización de la información de cada alumno.

## Objetivo

El propósito de esta aplicación es demostrar la integración entre Android y Supabase para la consulta de datos en tiempo real, permitiendo filtrar estudiantes por semestre y materia de manera sencilla e intuitiva.

## Requisitos

Antes de ejecutar el proyecto, asegúrate de contar con lo siguiente:

* Android Studio instalado.
* Emulador o dispositivo Android con API 34 o superior.
* Proyecto configurado en Supabase con las tablas `alumnos` y `materias`.

## Configuración del Proyecto

1. Clona este repositorio en tu equipo.
2. Abre el archivo `local.properties` ubicado en la raíz del proyecto y agrega tus credenciales de Supabase:

```properties
SUPABASE_URL=https://tu-proyecto.supabase.co
SUPABASE_KEY=tu_anon_key
```

3. Sincroniza las dependencias desde Android Studio mediante la opción **File → Sync Project with Gradle Files**.
4. Ejecuta la aplicación presionando **Run**.

## Funcionamiento

La aplicación permite realizar consultas de estudiantes a través de dos filtros principales:

1. Seleccionar un **semestre** desde el Spinner.
2. Elegir una **materia** correspondiente al semestre seleccionado.
3. Visualizar la lista de alumnos registrados, mostrando información como:

    * Fotografía del estudiante.
    * Nombre completo.
    * Correo electrónico.
    * Número de teléfono.

## Tecnologías Utilizadas

* Kotlin
* Android SDK
* Supabase (Postgrest)
* ListView personalizado mediante `AlumnoAdapter`
* Glide para la carga y transformación de imágenes (`CircleCrop`)
* Componentes de interfaz: Spinner, ImageView, TextView, ConstraintLayout y LinearLayout

## Evidencias de Funcionamiento



<img width="513" height="956" alt="image" src="https://github.com/user-attachments/assets/b86fe6f2-89b5-41c5-b8d8-c36deb0461dc" />


## Repositorio del Proyecto

https://github.com/yaser-uwu/tareakotlinsupabase
