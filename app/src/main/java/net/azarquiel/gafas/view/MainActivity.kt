    package net.azarquiel.gafas.view

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import net.azarquiel.gafas.navigation.AppNavigation
import net.azarquiel.gafas.ui.theme.GafasTheme
import net.azarquiel.gafas.viewmodel.MainViewModel

    class MainActivity : ComponentActivity() {
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            val viewModel = MainViewModel(this)
            setContent {
                GafasTheme {
                    AppNavigation(viewModel)
                }
            }
        }
    }


