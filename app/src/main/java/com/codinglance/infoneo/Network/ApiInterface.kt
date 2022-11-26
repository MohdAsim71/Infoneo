package com.codinglance.infoneo.Network
import com.codinglance.infoneo.model.ImageResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiInterface
{
    companion object{
        const val BASE_URL="https://dtu.infoneotech.com/api/"
    }

    @Multipart
    @POST("UploadFile")
    suspend fun uploadImage(@Part part: MultipartBody.Part):ImageResponse



}