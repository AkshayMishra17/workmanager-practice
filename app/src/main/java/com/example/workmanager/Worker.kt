package com.example.workmanager

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class ImageUploadWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        val imageUri = inputData.getString("IMAGE_URI") ?: return Result.failure()

        try {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

            // Use a task to upload the file and then send notification
            val uploadTask = imageRef.putFile(Uri.parse(imageUri))

            uploadTask.addOnSuccessListener {
                Log.d("ImageUploadWorker", "Image upload successful")
                sendNotification("Upload Successful", "Your image has been uploaded.")
            }.addOnFailureListener { e ->
                Log.e("ImageUploadWorker", "Image upload failed", e)
                sendNotification("Upload Failed", "Failed to upload your image.")
            }

            // Wait for the upload to finish
            return Result.success()
        } catch (e: Exception) {
            Log.e("ImageUploadWorker", "Error uploading image", e)
            return Result.failure()
        }
    }

    @SuppressLint("NotificationPermission")
    private fun sendNotification(title: String, message: String) {
        // Create notification channel (if needed)
        val channelId = "upload_channel"
        createNotificationChannel(applicationContext, channelId)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Added FLAG_IMMUTABLE for better compatibility
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_upload) // Set your own icon here
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification) // Notification ID can be any integer
    }
}
