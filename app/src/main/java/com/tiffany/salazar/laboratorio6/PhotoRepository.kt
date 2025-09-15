package com.tiffany.salazar.laboratorio6

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para obtener fotos desde Pexels API.
 * Se encarga de configurar el Pager y exponer un flujo de datos paginados.
 */
class PhotoRepository(private val apiKey: String) {

    /**
     * Devuelve un flujo de PagingData<Photo> para una consulta dada.
     */
    fun getPhotosStream(query: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { RealPhotoPagingSource(apiKey, query) }
        ).flow
    }
}
