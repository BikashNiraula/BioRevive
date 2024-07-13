package com.whatever.biorevive

import android.util.Log
import android.widget.Toast
import com.whatever.biorevive.database.FaceData

class FaceMatcher {

    //using l2 norm
    fun findNearest(storedFaceEmbeddingList:List<FaceData>, unknownFaceEmbedding:FloatArray):Triple<String,String,Float>{
        //here first is name
        // second is rollNo
        //third is the lowest distance value
        // var ret = Triple<String,String,Float>("","",Float.MAX_VALUE)
        //l2 norm
//        for(i in 0..storedFaceEmbeddingList.size-1){
//            var  distance = 0f;
//            //calculating the diff between each embedding value
//            for(j in 0..unknownFaceEmbedding.size-1){
//                val diff = unknownFaceEmbedding[j] - storedFaceEmbeddingList[i].faceEmbedding[j]
//                distance +=diff*diff
//            }
//            distance = Math.sqrt(distance.toDouble()).toFloat()
//            if(distance < ret.third){
//                ret = Triple(storedFaceEmbeddingList[i].name,
//                    storedFaceEmbeddingList[i].rollNo,distance)
//            }
//        }

        var ret = Triple<String,String,Float>("","",- 2.0f)
        var cosineValue = 0.0f
        var faceEmbedding = floatArrayOf()
//
//        //cosine similarity
//        // -1 is opposite
//        // 0 is orthogonality
//        // 1 is similar
        for(i in 0..storedFaceEmbeddingList.size-1){
            var dotproduct = 0.0f
            var normA = 0.0f
            var normB = 0.0f
            for(j in 0..unknownFaceEmbedding.size-1){
                dotproduct += storedFaceEmbeddingList[i].faceEmbedding[j] * unknownFaceEmbedding[j]
                normA += unknownFaceEmbedding[j] * unknownFaceEmbedding[j]
                normB += storedFaceEmbeddingList[i].faceEmbedding[j] * storedFaceEmbeddingList[i].faceEmbedding[j]

            }
            cosineValue = dotproduct/(Math.sqrt(normA.toDouble()) * Math.sqrt(normB.toDouble())).toFloat()

            if(cosineValue>ret.third){
                ret = Triple(storedFaceEmbeddingList[i].name,
                    storedFaceEmbeddingList[i].rollNo,cosineValue)
                Log.d("FaceEmbedding","unknownFaceEmbedding${unknownFaceEmbedding.contentToString()} cosinevalues:${cosineValue}")
            }
        }
        //cosine threshold is 0.4f
        if(cosineValue<0.4f){
            ret = Triple("unknown face",
                "unknown",cosineValue)
        }
        return ret
    }
}