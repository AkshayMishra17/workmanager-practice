package com.example.workmanager

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ImageUploadWorker(context:Context,params: WorkerParameters): Worker(context,params)
{
    override fun doWork(): Result {

        val imageUri = inputData.getString("IMAGE_URI") ?: return Result.failure()

        try{
            val storageRef = FirebaseStorage.getInstance().reference

            val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

            val uploadTask = imageRef.putFile(Uri.parse(imageUri))

            uploadTask.addOnSuccessListener {
                Log.d("ImageUploadWorker","Image upload successful")
            }.addOnFailureListener{e ->
                Log.e("ImageUploadWorker","Image upload failed",e)
            }
            return Result.success()

        }catch(e: Exception){

         Log.e("ImageUploadWorker","Error uploading image",e)
            return Result.failure()

        }

    }
}