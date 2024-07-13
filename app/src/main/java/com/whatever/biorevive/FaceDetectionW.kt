package com.whatever.biorevive

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.tasks.await


class FaceDetectionW {

    private var highAccuracyOpts: FaceDetectorOptions
    private var detector: FaceDetector
    //set face detection options in higher accuracy
    // High-accuracy landmark detection and face classification
    init {
        highAccuracyOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
            .build()
        //initializing the face detector
        detector = FaceDetection.getClient(highAccuracyOpts)
    }







suspend fun performFaceDetection(bitmap:Bitmap):Bitmap{
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutableBitmap)
        var imageBitmap = mutableBitmap
        val image = InputImage.fromBitmap(bitmap, 0)
        val croppedFaceBitmapList:MutableList<Bitmap> = mutableListOf()
        var i = 0


        detector.process(image)
            .addOnSuccessListener { faces ->
                // Task completed successfully
                // ...
                FacePresence.faceList = faces
                for (face in faces) {
                    i++
                    Log.d("Faces", "${i}")
                    val bounds = face.boundingBox
                    val paint = Paint()
                    paint.color = Color.BLUE
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = 3F
                    canvas.drawRect(bounds,paint)
                    imageBitmap = croppedFaceBitmap(bounds,bitmap)
                    croppedFaceBitmapList.add(imageBitmap)
                    Log.d("Faces", "are they equal inside success listener:${imageBitmap==mutableBitmap}")



                }



                //binding.imageChoosen.setImageBitmap(mutableBitmap)
            }
            .addOnFailureListener { e ->
                Log.d("Detection", "Face Detection Failed:${e.message}")
                // Task failed with an exception
                // ...
            }
            // waits for the task to finish
            .await()

    if(croppedFaceBitmapList.isEmpty()){
        FacePresence.isFacePresent = false
        return mutableBitmap
    }

    FacePresence.isFacePresent = true
    Log.d("Faces", "are they equal before returning:${imageBitmap==mutableBitmap}")
        return croppedFaceBitmapList[0]
    }
    private fun croppedFaceBitmap(bound: Rect, bitmap: Bitmap): Bitmap {
        //cropping the face
        if (bound.top < 0) {
            bound.top = 0
        }
        if (bound.left < 0) {
            bound.left = 0
        }
        if (bound.right > bitmap.width) {
            bound.right = bitmap.width - 1
        }
        if (bound.bottom > bitmap.height) {
            bound.bottom = bitmap.height - 1
        }
        val croppedBitmapImage  = Bitmap.createBitmap(bitmap, bound.left, bound.top, bound.width(), bound.height())
        return croppedBitmapImage
    }


}