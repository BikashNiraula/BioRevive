package com.whatever.biorevive.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.content.ContextCompat
import com.whatever.biorevive.FaceDetectionW
import com.whatever.biorevive.FaceMatcher
import com.whatever.biorevive.Facenet
import com.whatever.biorevive.database.DatabaseManager
import com.whatever.biorevive.databinding.ActivityRecognitionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias faceListener = (Bitmap)->Unit

class RecognitionActivity : AppCompatActivity() {
    private lateinit var faceDetectionW:FaceDetectionW
    private lateinit var facenet:Facenet
    var cameraSelectedFront = true

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSION_ARRAY && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(baseContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                startCamera()
            }
        }

    private lateinit var binding: ActivityRecognitionBinding

    private lateinit var cameraExecutor:ExecutorService

    private var imageCapture: ImageCapture? = null

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private var isProcessing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecognitionBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        //request permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        // Set up the listeners for take photo and video capture buttons
        binding.imageCaptureButton.setOnClickListener { takePhoto() }
        //toggle the camera selector
        binding.cameraSelectorButton.setOnClickListener {
            cameraSelectedFront  = !cameraSelectedFront
            startCamera()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
        faceDetectionW = FaceDetectionW()
        facenet = Facenet(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun captureVideo() {
        TODO("Not yet implemented")
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this), // Defines where the callbacks are run
            object : ImageCapture.OnImageCapturedCallback() {

                @OptIn(ExperimentalGetImage::class)
                override fun onCaptureSuccess(imageProxy: ImageProxy) {
                    //val image: Image? = imageProxy.image // Do what you want with the image
                    var bitmap = imageProxy.toBitmap()
                    val matrix = Matrix()
                    matrix.setRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
                    // Apply the rotation to the Bitmap
                    bitmap = Bitmap.createBitmap(bitmap,
                    0,
                    0,
                        bitmap.width,
                        bitmap.height,
                    matrix,
                    true
                )


                    CoroutineScope(Dispatchers.Main).launch {
                        val t1 = System.currentTimeMillis()
                        //val croppedImage = bitmapImage
                        val croppedImage = faceDetectionW.performFaceDetection(bitmap)


                        val faceEmbedding = facenet.getFaceEmbedding(croppedImage)
                        val storedFaceEmbeddingList =
                            DatabaseManager(applicationContext).database.dao.getFaceData()
                        //Log.d("FaceEmbedding", "faceEmbedding:${storedFaceEmbeddingList[2].faceEmbedding}\nname:${storedFaceEmbeddingList[2].name} ")
                        val triple =
                            FaceMatcher().findNearest(storedFaceEmbeddingList, faceEmbedding)
                        binding.imageView.setImageBitmap(croppedImage)
                        //Log.d("ImageAnalysis","The time taken for the analysis is ${System.currentTimeMillis() -t1}")
                        binding.tvFaceData.setText("name:${triple.first} rollNO:${triple.second} cosineSimilarity:${triple.third}")
                        imageProxy.close() // Make sure to close the image
                    }




                }

                override fun onError(exception: ImageCaptureException) {
                    // Handle exception
                    Log.d("RecognitionActivity", "${exception.printStackTrace()}")
                }
            }
        )
    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSION_ARRAY)
    }
    private fun startCamera(){
        val cameraFutureProvider = ProcessCameraProvider.getInstance(this)

        cameraFutureProvider.addListener({
            val cameraProvider: ProcessCameraProvider = cameraFutureProvider.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()
//            val imageAnalyzer = ImageAnalysis.Builder()
//                .build()
//                .also {
//                    it.setAnalyzer(cameraExecutor, FaceAnalyzer { bitmapImage ->
//                        CoroutineScope(Dispatchers.Main).launch {
//                            val t1 = System.currentTimeMillis()
//                            //val croppedImage = bitmapImage
//                            val croppedImage = faceDetectionW.performFaceDetection(bitmapImage)
//
//
//                            val faceEmbedding = facenet.getFaceEmbedding(croppedImage)
//                            val storedFaceEmbeddingList =
//                                DatabaseManager(applicationContext).database.dao.getFaceData()
//                            //Log.d("FaceEmbedding", "faceEmbedding:${storedFaceEmbeddingList[2].faceEmbedding}\nname:${storedFaceEmbeddingList[2].name} ")
//                            val triple =
//                                FaceMatcher().findNearest(storedFaceEmbeddingList, faceEmbedding)
//                            //Log.d("ImageAnalysis","The time taken for the analysis is ${System.currentTimeMillis() -t1}")
//                            binding.tvFaceData.setText("name:${triple.first} rollNO:${triple.second} cosineSimilarity:${triple.third}")
//
//
//                        }
//                    })
//                }
            val cameraSelector = when(cameraSelectedFront){
                true->CameraSelector.DEFAULT_FRONT_CAMERA
                else->CameraSelector.DEFAULT_BACK_CAMERA
            }


            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (exc: Exception) {
                Log.e("Camera", "The message is ${exc.message}")
            }


        }, ContextCompat.getMainExecutor(this))


    }

    fun allPermissionsGranted():Boolean{
        return REQUIRED_PERMISSION_ARRAY.all {
            ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
        }
    }


    companion object{
        val REQUIRED_PERMISSION_ARRAY = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
        }.toTypedArray()
    }

//    inner class FaceAnalyzer(private val faceListener: faceListener): ImageAnalysis.Analyzer{
//
//
//
//        override fun analyze(image: ImageProxy) {
//
////            if (isProcessing || FacePresence.faceList.size == 0) {
////                //image.close()
////                return
//            var bitmapImage = image.toBitmap()
//
//                isProcessing = true
//
//
//                val matrix = Matrix()
//                matrix.setRotate(image.imageInfo.rotationDegrees.toFloat())
//                // Apply the rotation to the Bitmap
//                bitmapImage = Bitmap.createBitmap(
//                    bitmapImage,
//                    0,
//                    0,
//                    bitmapImage.width,
//                    bitmapImage.height,
//                    matrix,
//                    true
//                )
//
////            CoroutineScope(Dispatchers.Main).launch{
////                binding.imageView.setImageBitmap(bitmapImage)
////            }
//                Log.d("ImageAnalysis", "after bitmap IMage")
//
////            val defferdJob = CoroutineScope(Dispatchers.Main).async {
////                val t1 = System.currentTimeMillis()
////                var croppedImage = bitmapImage
////                if(bitmapImage != null) {
////                    val croppedImage = faceDetectionW.performFaceDetection(bitmapImage)
////                } else{
////                    Log.d("ImageAnalysis", "The bitmap value is null ${bitmapImage}")
////                }
////                val faceEmbedding = facenet.getFaceEmbedding(croppedImage)
////                val storedFaceEmbeddingList = DatabaseManager(applicationContext).database.dao.getFaceData()
////                val triple = FaceMatcher().findNearest(storedFaceEmbeddingList, faceEmbedding)
////                Log.d("ImageAnalysis","The time taken for the analysis is ${System.currentTimeMillis() -t1}")
////            }
//
//                faceListener(bitmapImage)
//
//                Log.d("ImageAnalysis", "The image is closing immediately")
//                image.close()
//
//
//        }
//
//    }
}