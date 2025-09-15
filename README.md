# Laboratorio 6 – Búsqueda con Debounce, Scroll Infinito y Navegación a Detalles

Este proyecto es una aplicación Android construida con **Kotlin** y **Jetpack Compose**. Permite buscar fotos usando una barra de búsqueda con debounce, mostrar los resultados en una grilla con scroll infinito, ver detalles de cada foto y acceder a una pantalla de perfil.

## Manejo de Estado sin ViewModel

En este laboratorio no se utiliza ViewModel. Todo el estado se administra directamente en los composables usando `remember` y `rememberSaveable`. Esto mantiene la lógica simple y evita dependencias adicionales.

* **`remember`** se usa para valores que solo deben mantenerse durante recomposiciones.
* **`rememberSaveable`** se usa para valores que deben sobrevivir rotaciones de pantalla o recreación de actividad.

### Ejemplo

```kotlin
var query by rememberSaveable { mutableStateOf("") }

LaunchedEffect(query) {
    snapshotFlow { query }
        .debounce(500)
        .collect { newQuery ->
            repository.searchPhotos(newQuery)
        }
}
```

En este caso:

* `rememberSaveable` mantiene el texto de búsqueda al rotar la pantalla.
* `snapshotFlow` observa los cambios de `query`.
* `debounce(500)` evita llamadas excesivas a la API al escribir rápido.

## Características principales

* Búsqueda con debounce de 500 ms.
* Grilla de fotos con `LazyVerticalGrid`.
* Scroll infinito con Paging 3.
* Navegación Compose: Home → Details → Profile.
* Carga de imágenes con Coil.
* Pantalla de detalles con imagen, autor y datos básicos.
* Pantalla de perfil con avatar, nombre, correo y switch de tema.

## Limitaciones de no usar ViewModel

Aunque este enfoque es simple, tiene limitaciones:

* Si el proceso es finalizado por el sistema, parte del estado puede perderse.
* En aplicaciones grandes, mantener todo el estado en composables puede dificultar la organización y pruebas.

En proyectos más complejos, podría ser conveniente usar ViewModel para separar la lógica de negocio, manejar mejor el ciclo de vida y soportar restauración completa de estado.

## Tecnologías

* Kotlin
* Jetpack Compose + Material 3
* Navigation Compose
* Paging 3
* Retrofit + API de Pexels
* Coil
