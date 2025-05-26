# BioRevive

**BioRevive** is an offline Android-based attendance system that leverages facial recognition to automate and streamline attendance tracking. Built using Kotlin, it combines real-time face detection via Google's ML Kit with accurate facial recognition using a pre-trained FaceNet model deployed through TensorFlow Lite.

---

## 🧠 Key Features

- 🔍 **Face Detection**: Utilizes **ML Kit** for detecting faces in real-time from a mobile device's camera.
- 🧬 **Face Recognition**: Uses a **TensorFlow Lite** version of the **FaceNet** model to recognize and verify individuals.
- 📴 **Offline Capability**: Designed to function **completely offline**, ensuring privacy and usability in low-connectivity environments.
- 📱 **Mobile-First**: Built for Android devices using **Kotlin** and the **Android SDK**.

---

## 📸 How It Works

1. **Face Detection**: ML Kit identifies the position of a face in the camera feed.
2. **Embedding Generation**: The TFLite FaceNet model generates an embedding vector from the face.
3. **Recognition**: The embedding is compared with stored data to identify the user.
4. **Attendance Logging**: If a match is found, attendance is marked and stored locally.

---

## 🚀 Getting Started

### Prerequisites

- Android Studio (latest version recommended)
- Android device or emulator (Android 8.0+)

### Installation

1. Clone this repository:
   ```bash
   git clone https://github.com/BikashNiraula/BioRevive.git
2. Open the project in Android Studio.
3. Build the project to generate the APK file.
4. Install the APK on your Android device.
