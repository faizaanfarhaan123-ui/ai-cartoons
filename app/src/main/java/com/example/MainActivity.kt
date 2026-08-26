package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.navigation.AppNavigation
import com.example.ui.theme.CreatorDashTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import android.util.Log

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                val apiKey = BuildConfig.FIREBASE_API_KEY
                val appId = BuildConfig.FIREBASE_APP_ID
                val projectId = BuildConfig.FIREBASE_PROJECT_ID
                
                if (apiKey.isNotEmpty() && appId.isNotEmpty() && projectId.isNotEmpty() && apiKey != "your-api-key") {
                    val options = FirebaseOptions.Builder()
                        .setApiKey(apiKey)
                        .setApplicationId(appId)
                        .setProjectId(projectId)
                        .build()
                    FirebaseApp.initializeApp(this, options)
                    Log.d("FirebaseInit", "Firebase initialized with custom options")
                } else {
                    val dummyOptions = FirebaseOptions.Builder()
                        .setApiKey("dummy-api-key")
                        .setApplicationId("1:1234567890:android:dummy")
                        .setProjectId("dummy-project")
                        .build()
                    FirebaseApp.initializeApp(this, dummyOptions)
                    Log.d("FirebaseInit", "Firebase initialized with dummy options (credentials missing)")
                }
            }
        } catch (e: Exception) {
            Log.e("FirebaseInit", "Error initializing Firebase: ${e.message}")
        }
        
        enableEdgeToEdge()
        setContent {
            CreatorDashTheme {
                AppNavigation()
            }
        }
    }
}
