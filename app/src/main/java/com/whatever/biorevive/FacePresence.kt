package com.whatever.biorevive

import android.graphics.Matrix
import com.google.mlkit.vision.face.Face


object FacePresence {
    var isFacePresent = false
    var faceList = mutableListOf<Face>()
}