package com.codinglance.infoneo
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.codinglance.infoneo.databinding.ActivityMainBinding
import com.codinglance.infoneo.repository.ResultState
import com.codinglance.infoneo.viewModel.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var  binding:ActivityMainBinding
    private val imageViewModel: ImageViewModel by viewModels()
    private lateinit var mProgressDialog: ProgressDialog
    private var  imageResult: Any? = null

    companion object{
         var imageUri: Uri? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rootView = layoutInflater.inflate(R.layout.activity_main, null, true)
        binding = DataBindingUtil.bind<ActivityMainBinding>(rootView)!!
        setContentView(rootView)
        supportActionBar?.hide()
        binding.viewModel= imageViewModel

        binding.upRl.setOnClickListener {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, 0)
    }

        initViewModel()


    }
    private fun initViewModel() {
        imageViewModel.imagestate.observe(this){
            when(it){
                ResultState.Loading -> {
                    displayProgressAnimation()
                }
                is ResultState.Success ->{
                    hideProgressAnimation()

                    Glide.with(this).load(it.data.fullurl).into(binding.resultImg);

                }
                is ResultState.Error ->{
                    Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()

                }

            }
        }
    }
    fun displayProgressAnimation() {

        // only create one
        if(!this::mProgressDialog.isInitialized) {
            // start progress Dialog animation:
            mProgressDialog = ProgressDialog.show(
                this,
                null,
                "LOADING...",
                false
            )
        } else {
            mProgressDialog.show()
        }
    }


    fun hideProgressAnimation() {

        mProgressDialog.let {
            //if (it.isShowing)
            mProgressDialog.dismiss()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (requestCode == 0 && resultCode == RESULT_OK && null != data)
            {
                imageUri = data?.data
                binding.pickImg.setImageURI(imageUri)
                imageResult=imageViewModel.apiCall()
            }
        }
        catch (e:Exception)
        {

        }
    }
}



