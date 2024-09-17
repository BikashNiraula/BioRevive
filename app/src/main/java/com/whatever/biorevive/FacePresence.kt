package com.whatever.biorevive

import com.google.mlkit.vision.face.Face


object FacePresence {
    var isFacePresent = false
    var faceList = mutableListOf<Face>()
}