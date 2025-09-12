package com.tiffany.salazar.laboratorio6

import androidx.paging.PagingSource
import androidx.paging.PagingState

class PhotoPagingSource(private val query: String) : PagingSource<Int, Photo>(){ //PagingSource falso (pruebas locales sin internet)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo>{
        return try{
            val page = params.key ?: 1

            val photos = PhotoRepository.getPhotosStream(query/*page, params.loadSize*/) //API falso de ejemplo

            LoadResult.Page(
                data = photos,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (photos.isEmpty()) null else page + 1
            )
        }catch(e: Exception){
            LoadResult.Error(e) //Error inesperado
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int?{
        return state.anchorPosition?.let { pos -> //Se define la clave de refresco para reiniciar el scroll
            state.closestPageToPosition(pos)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(pos)?.nextKey?.minus(1)
        }
    }
}
