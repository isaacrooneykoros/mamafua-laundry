package com.example.thefinaldedication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.thefinaldedication.navigation.AppNavHost
import com.example.thefinaldedication.ui.theme.TheFinalDedicationTheme
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase

class MainActivity : ComponentActivity() {
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        // Initialize Firebase with the specific URL
        database = FirebaseDatabase.getInstance()
        database.setPersistenceEnabled(true)
        database.reference.keepSynced(true)

        setContent {
            val navController = rememberNavController()
            TheFinalDedicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavHost(navController = navController)
                }
            }
        }
    }
}
