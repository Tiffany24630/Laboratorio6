package com.tiffany.salazar.laboratorio6

import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

/**
 * PagingSource para obtener fotos desde la API real de Pexels.
 * Usa RetrofitClient y maneja la paginación.
 */
class RealPhotoPagingSource(
    private val apiKey: String,
    private val searchQuery: String
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: 1

        return try {
            val perPage = params.loadSize

            // Llamada a la API de Pexels
            val response = RetrofitClient.api.searchPhotos(
                apiKey = apiKey,
                query = searchQuery.ifBlank { "nature" },
                page = page,
                perPage = perPage
            )

            // Mapeo de DTO → modelo de dominio
            val photos = response.photos.map {
                Photo(
                    id = it.id.toString(),
                    imageUrl = it.src.medium,
                    description = it.alt,
                    author = it.photographer,
                    likes = 0 // Pexels no devuelve likes
                )
            }

            // Resultado de la página
            LoadResult.Page(
                data = photos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (photos.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e) // Error de red
        } catch (e: HttpException) {
            LoadResult.Error(e) // Error HTTP
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        // Define la clave de refresco para reiniciar el scroll
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }
    }
}
