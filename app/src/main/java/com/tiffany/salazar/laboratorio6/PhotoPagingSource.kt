package com.tiffany.salazar.laboratorio6

import androidx.paging.PagingSource
import androidx.paging.PagingState

/**
 * PagingSource falso (Fake API).
 * Útil para pruebas locales sin conexión a Internet.
 */
class PhotoPagingSource(private val query: String) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val page = params.key ?: 1

            // Fake API de ejemplo
            val photos = FakePhotoApi.searchPhotos(query, page, params.loadSize)

            LoadResult.Page(
                data = photos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (photos.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e) // Error inesperado
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
