package com.tiffany.salazar.laboratorio6

class FakePhotoApi(val photos: List<Photo>){
    suspend fun searchPhotos(query: String, page: Int, perPage: Int): List<Photo>{
        return photos.filter{ it.description?.contains(query) == true }.drop((page - 1) * perPage).take(perPage)
    }
}