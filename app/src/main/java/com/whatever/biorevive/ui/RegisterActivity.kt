package com.whatever.biorevive.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.whatever.biorevive.FaceDetectionW
import com.whatever.biorevive.FacePresence
import com.whatever.biorevive.Facenet
import com.whatever.biorevive.database.DatabaseManager
import com.whatever.biorevive.database.FaceData
import com.whatever.biorevive.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import kotlin.coroutines.resume

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private  var cameraImageUri: Uri? = null
    private lateinit var facenet: Facenet
    private lateinit var faceDetect: FaceDetectionW


    private val galleryActivityResultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
            //getting uri of image
            val galleryImageUri = it.data?.data
            //changing uri to bitmap
            val galleryImageBitmap = uriToBitmap(galleryImageUri)
            //correcting the orientation of the bitmap
            val rotatedGalleryImageBitmap = rotateBitmap(galleryImageBitmap,galleryImageUri)
            //create a coroutine
            var bitmapImage = rotatedGalleryImageBitmap
            binding.imageChoosen.setImageBitmap(bitmapImage)
            CoroutineScope(Dispatchers.Main).launch{
                bitmapImage = faceDetect.performFaceDetection(rotatedGalleryImageBitmap)
                val faceEmbbeding = facenet.getFaceEmbedding(bitmapImage)
                Log.i("FaceEmbedding", "${faceEmbbeding.contentToString()}")
                Toast.makeText(this@RegisterActivity, "${faceEmbbeding.contentToString()}", Toast.LENGTH_LONG).show()
                Log.d("Faces","bitmap Obtained in register activity inside coroutines:${bitmapImage == rotatedGalleryImageBitmap}")
                binding.imageChoosen.setImageBitmap(bitmapImage)

                if(!FacePresence.isFacePresent){
                    Toast.makeText(this@RegisterActivity, "There is no face present!!!", Toast.LENGTH_SHORT).show()
                } else{
                    val dialog = showInfoDialog()
                    val faceNameList=getFaceNameListFromDialog(dialog)
                    Log.d("Dialog", "outside ${faceNameList.toString()}")
                    if(faceNameList.isNotEmpty()) {
                        //save face details to database
                        Log.d("Dialog","inside here")
                        val faceData = FaceData(name = faceNameList[0], rollNo = faceNameList[1], faceEmbbeding)
                        DatabaseManager(applicationContext).database.dao.insertFaceData(faceData)

                    }
                }
            }


            Log.d("Faces","bitmap Obtained in register activity:${bitmapImage == rotatedGalleryImageBitmap}")

            //Log.i("Faces", "${faceList.size}")

        })



    private val cameraActivityResultLauncher: ActivityResultLauncher<Intent>
            = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK){
            val cameraImageBitmap = uriToBitmap(cameraImageUri)
            val rotatedCameraImageBitmap = rotateBitmap(cameraImageBitmap,cameraImageUri)
            var bitmapImage = rotatedCameraImageBitmap
            binding.imageChoosen.setImageBitmap(bitmapImage)
            CoroutineScope(Dispatchers.Main).launch{
                bitmapImage = faceDetect.performFaceDetection(rotatedCameraImageBitmap)
                val faceEmbbeding = facenet.getFaceEmbedding(bitmapImage)
                Log.i("FaceEmbedding", "${faceEmbbeding.size}")
                binding.imageChoosen.setImageBitmap(bitmapImage)
                if(!FacePresence.isFacePresent){
                    Toast.makeText(this@RegisterActivity, "There is no face present!!!", Toast.LENGTH_SHORT).show()
                } else{
                    val dialog = showInfoDialog()
                    val faceNameList=getFaceNameListFromDialog(dialog)
                    Log.d("Dialog", "outside ${faceNameList.toString()}")
                    if(faceNameList.isNotEmpty()) {
                        //save face details to database
                        Log.d("Dialog","inside here")
                        val faceData = FaceData(name = faceNameList[0], rollNo = faceNameList[1], faceEmbbeding)
                        DatabaseManager(applicationContext).database.dao.insertFaceData(faceData)

                    }
                }

            }

        }
    }
    suspend fun getFaceNameListFromDialog(dialog: InfoDialogBox):List<String>{

        return  suspendCancellableCoroutine{ continuation->
            val list = mutableListOf<String>()
            dialog.yesButton.setOnClickListener {
                val name = dialog.nameEditText.text.toString()
                val rollNo = dialog.rollNoEditText.text.toString()
                list.add(name)
                list.add(rollNo)
                Toast.makeText(
                    this@RegisterActivity,
                    "Name:${name} RollNo:${rollNo}",
                    Toast.LENGTH_LONG
                ).show()
                continuation.resume(list)
                dialog.dismiss()
                Log.d("EmptyList", "The faceNameList inside on clicklistener:${list.size}")
            }
            dialog.cancelButton.setOnClickListener {
                dialog.dismiss()
            }



        }


    }
    private fun showInfoDialog(): InfoDialogBox {
        val dialog = InfoDialogBox(this)
        dialog.show()
        return dialog

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        //Initializing the face detector


        binding.btnInsertImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryActivityResultLauncher.launch(galleryIntent)


        }
        binding.btnImageCapture.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(ContextCompat.checkSelfPermission(this,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_DENIED ||
                    ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED){
                    val permissionArray = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    requestPermissions(permissionArray, 122)
                } else{
                    openCamera()
                }
            } else{
                openCamera()
            }
        }

        faceDetect = FaceDetectionW()
        //initializing the facenet model
        facenet = Facenet(this)


    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        cameraImageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,cameraImageUri)
        cameraActivityResultLauncher.launch(intent)
        //infoFillUpDialog()

    }

    private fun uriToBitmap(imURi:Uri?): Bitmap? {
        try {


            val parcelFileDescriptor = imURi?.let { contentResolver?.openFileDescriptor(it, "r") }?:return null
                ?:return null

            val fileDescriptor = parcelFileDescriptor!!.fileDescriptor
            val image = BitmapFactory.decodeFileDescriptor(fileDescriptor)
            parcelFileDescriptor.close()
            return image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
    //TODO rotate image if image captured on samsung devices
//TODO Most phone cameras are landscape, meaning if you take the photo in portrait, the resulting photos will be rotated 90 degrees.
    @SuppressLint("Range")
    fun rotateBitmap(input: Bitmap?, imageUri:Uri?): Bitmap {
        val orientationColumn =
            arrayOf(MediaStore.Images.Media.ORIENTATION)
        val cur: Cursor? = imageUri?.let {
            contentResolver.query(it, orientationColumn, null, null, null)
        }?:null

        var orientation = -1
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientationColumn[0]))
        }
        cur?.close()
        Log.d("tryOrientation", orientation.toString() + "")
        val rotationMatrix = Matrix()
        rotationMatrix.setRotate(orientation.toFloat())
        return input?.let {
            Bitmap.createBitmap(it, 0, 0, input.width, input.height, rotationMatrix, true)
        }?:Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888)
    }


}