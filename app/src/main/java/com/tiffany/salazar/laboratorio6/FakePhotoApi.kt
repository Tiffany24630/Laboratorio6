package com.tiffany.salazar.laboratorio6

/**
 * API falsa para pruebas locales sin conexión a internet.
 * Genera fotos dummy para simular paginación.
 */
object FakePhotoApi {

    fun searchPhotos(query: String, page: Int, perPage: Int): List<Photo> {
        val start = (page - 1) * perPage
        val end = start + perPage

        return (start until end).map { index ->
            Photo(
                id = index.toString(),
                imageUrl = "https://picsum.photos/300/300?random=$index",
                description = "Foto de prueba #$index para '$query'",
                author = "Autor $index",
                likes = (0..500).random()
            )
        }
    }
}
