package com.codinglance.infoneo.repository

import com.codinglance.infoneo.Network.ApiInterface
import com.codinglance.infoneo.model.ImageResponse
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val  service: ApiInterface
)
{
    suspend fun uploadImage(image:MultipartBody.Part): ImageResponse {
        return service.uploadImage(image)
    }
}