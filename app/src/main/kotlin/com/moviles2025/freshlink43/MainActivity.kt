// MainActivity.kt
package com.moviles2025.freshlink43

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.compose.rememberNavController
import com.moviles2025.freshlink43.ui.navigation.NavGraph
import com.moviles2025.freshlink43.utils.FreshLinkTheme
import com.moviles2025.freshlink43.network.ConnectivityHandler
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.compose.ui.Modifier

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var connectivityHandler: ConnectivityHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = getColor(R.color.white)
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true

        setContent {
            FreshLinkTheme(darkTheme = false) {
                val navController = rememberNavController()

                val isConnected by connectivityHandler.isConnected.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }
                var lastConnectionState by remember { mutableStateOf<Boolean?>(null) }

                LaunchedEffect(isConnected) {
                    // Solo mostrar si ya hubo al menos un valor previo
                    if (lastConnectionState != null && lastConnectionState != isConnected) {
                        val message =
                            if (isConnected) "Internet connected"
                            else "We lost internet connection"
                        // Aquí le damos un botón "Cerrar" para que el usuario lo descarte
                        snackbarHostState.showSnackbar(
                            message  = message,
                            actionLabel = "Cerrar"
                        )
                    }
                    // Actualizamos el estado para la próxima vez
                    lastConnectionState = isConnected
                }

                Scaffold(
                    // Con esto el botón "Cerrar" aparece junto al mensaje
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { paddingValues ->
                    NavGraph(
                        navController = navController,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}