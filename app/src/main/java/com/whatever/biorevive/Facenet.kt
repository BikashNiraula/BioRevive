package com.whatever.biorevive

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.common.TensorOperator
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.support.tensorbuffer.TensorBufferFloat
import java.nio.ByteBuffer
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

class Facenet(context: Context) {

    // TFLite interpreter for the face-net model
    private lateinit var interpreter: Interpreter

    // Input image size for FaceNet model.
    // 160 for facenet
    private val imgSize = 160
    private val useGpu = true
    private val useXNNPack = true

    // Image Processor for preprocessing input images.
    private val imageTensorProcessor = ImageProcessor.Builder()
        .add(ResizeOp(imgSize, imgSize, ResizeOp.ResizeMethod.BILINEAR))
        .add(StandardizeOp())
        .build()

    init {
        // Initialize TFLiteInterpreter

        val interpreterOptions = Interpreter.Options().apply {

                    // Number of threads for computation
                    setNumThreads(4)

        }
        interpreter =
            Interpreter(FileUtil.loadMappedFile(context, "facenet.tflite"), interpreterOptions)

    }

    // Resize the given bitmap and convert it to a ByteBuffer
    private fun convertBitmapToBuffer(image: Bitmap): ByteBuffer {
        val imageTensor = imageTensorProcessor.process(TensorImage.fromBitmap(image))
        return imageTensor.buffer
    }

    // Run the FaceNet model.
    private fun runFaceNet(inputs: Any): Array<FloatArray> {
        val t1 = System.currentTimeMillis()
        val outputs = Array(1) { FloatArray(128) }
        interpreter.run(inputs, outputs)
        Log.i("Performance", "FaceNet Inference Speed in ms : ${System.currentTimeMillis() - t1}")
        return outputs
    }

    fun getFaceEmbedding(image: Bitmap): FloatArray {
        return runFaceNet(convertBitmapToBuffer(image))[0]
    }

    class StandardizeOp : TensorOperator {
        override fun apply(tensorBuffer: TensorBuffer?): TensorBuffer {
            if (tensorBuffer == null) {
                Log.e("StandardizeOp", "TensorBuffer is null")
                throw IllegalArgumentException("TensorBuffer cannot be null")
            }


            val pixels = tensorBuffer.floatArray
            Log.d("StandardizeOp", "Pixels size: ${pixels.size}")

            val mean = pixels.average().toFloat()
            var std = sqrt(pixels.map { pi -> (pi - mean).pow(2) }.sum() / pixels.size.toFloat())
            std = max(std, 1f / sqrt(pixels.size.toFloat()))
            Log.d("StandardizeOp", "Mean: $mean, Std: $std")

            for (i in pixels.indices) {
                pixels[i] = (pixels[i] - mean) / std
            }
            val output = TensorBufferFloat.createFixedSize(tensorBuffer.shape, DataType.FLOAT32)

            output.loadArray(pixels)

            return output
        }
    }
}
