package com.codinglance.infoneo.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.*
import com.codinglance.infoneo.MainActivity.Companion.imageUri
import com.codinglance.infoneo.model.ImageResponse
import com.codinglance.infoneo.repository.Repository
import com.codinglance.infoneo.repository.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application)
{
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext
    private val _imageState: MutableLiveData<ResultState<ImageResponse>> = MutableLiveData()
    val imagestate: LiveData<ResultState<ImageResponse>> = _imageState
    fun apiCall() {
        viewModelScope.launch(Dispatchers.IO) {
            _imageState.postValue(ResultState.Loading)


            try {

                val file = getImageBody(imageUri!!)
                val requestFile: RequestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val multipartBody: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestFile)
                val response =  repository.uploadImage(multipartBody)
                _imageState.postValue(ResultState.Success(response))


            }

            catch (e:Exception){
                _imageState.postValue(ResultState.Error(e.localizedMessage.toString()))

            }

        }
    }


    fun getImageBody(imageUri: Uri): File {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(
            imageUri,
            "r",
            null
        )
        val file = File(
            context.cacheDir,
            context.contentResolver.getFileName(imageUri)
        )
        val inputStream = FileInputStream(parcelFileDescriptor?.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        return file
    }

    @SuppressLint("Range")
    fun ContentResolver.getFileName(uri: Uri): String {
        var name = ""
        val cursor = query(
            uri, null, null,
            null, null
        )
        cursor?.use {
            it.moveToFirst()
            name = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
        return name
    }
}