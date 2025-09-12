package com.tiffany.salazar.laboratorio6

import android.net.http.HttpException
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import java.io.IOException

class PhotoRepository(private val apiKey: String){ //Repositorio para obtener fotos desde Pexels API
    fun getPhotosStream(query: String): Flow<PagingData<Photo>>{ //Devuelve un flujo de PagingData<Photo> para una consulta
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RealPhotoPagingSource(apiKey, query) }
        ).flow
    }
    /*suspend fun searchPhotos(query: String, page: Int, perPage: Int): List<Photo>{
        if (query.isBlank()) {
            // Optionally, return popular photos or an empty list if query is blank
            // For now, let's return an empty list or throw an IllegalArgumentException
            // depending on desired behavior.
            // For PagingSource, returning an empty list might be better to signal end of data.
            return emptyList()
        }

        try {
            val response = PhotoRepository.getPhotosStream(
                apiKey = apiKey,
                query = query,
                page = page,
                perPage = perPage
            )

            if (response.isSuccessful) {
                // Return the list of photos from the response body, or an empty list if null
                return response.body()?.photos ?: emptyList()
            } else {
                // Throw an HttpException with error details from the response
                throw com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException(response)
            }
        } catch (e: IOException) {
            // Re-throw IOException for network issues (e.g., no internet)
            throw e
        } catch (e: HttpException) {
            // Re-throw HttpException for API error responses (4xx, 5xx)
            // The PagingSource will catch this and turn it into LoadResult.Error
            throw e
        } catch (e: Exception) {
            // Catch any other unexpected exceptions
            // Log.e("PhotoRepository", "Unexpected error in searchPhotos", e)
            throw Exception("An unexpected error occurred while fetching photos.", e)
        }
    }*/
}
