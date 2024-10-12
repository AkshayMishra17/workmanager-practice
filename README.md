# Image Uploader with WorkManager and Firebase

This is a proof of concept (POC) Android app demonstrating how to use `WorkManager` to upload images to Firebase Storage. The app showcases a simple implementation where the user selects an image, and the `WorkManager` handles the background upload task, even if the app is closed or the device is restarted.

## Features

- Image selection from device storage.
- Background image upload using `WorkManager.
- Uploads images to Firebase Storage.
- Displays upload status (success, failure, or progress).

## Tools and Libraries Used

- **WorkManager**: For scheduling and managing background image upload tasks.
- **Firebase Storage**: To store uploaded images securely in the cloud.
- **Kotlin**: For implementing the app.
- **Jetpack Compose**: For building UI components.

## Project Setup

### Prerequisites

- Android Studio installed.
- A Firebase project set up with Firebase Storage enabled.

### Steps to Set Up Firebase

1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/).
2. Enable Firebase Storage.
3. Add your `google-services.json` file to the `app` directory of your Android project.
4. Add the Firebase SDK dependencies in the `build.gradle` file:

   ```gradle
   implementation 'com.google.firebase:firebase-storage-ktx'
   implementation 'com.google.firebase:firebase-common-ktx'
