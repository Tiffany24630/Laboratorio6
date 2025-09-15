package com.tiffany.salazar.laboratorio6

/**
 * Data classes que representan la respuesta de la API de Pexels.
 */

data class PexelsResponse(
    val page: Int,
    val per_page: Int,
    val photos: List<PexelsPhoto>,
    val total_results: Int
)

data class PexelsPhoto(
    val id: Int,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    val photographer_url: String,
    val photographer_id: Int,
    val avg_color: String?,
    val src: PexelsPhotoSrc,
    val liked: Boolean,
    val alt: String?
)

data class PexelsPhotoSrc(
    val original: String,
    val large2x: String,
    val large: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String
)
