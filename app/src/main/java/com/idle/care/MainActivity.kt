package com.idle.care

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.idle.auth.AuthRoute
import com.idle.care.ui.theme.CareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CareTheme {
                Scaffold {
                    Box(modifier = Modifier.padding(it)) {
                        AuthRoute()
                    }
                }
            }
        }
    }
}