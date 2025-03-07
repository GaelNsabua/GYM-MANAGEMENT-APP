package com.example.gym_management_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.example.gym_management_app.R

@Composable
fun SplashScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // Fond noir
        contentAlignment = Alignment.Center // Centre le contenu
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_splach), // Remplace par ton logo
            contentDescription = "Logo",
            modifier = Modifier.size(200.dp) // Ajuste la taille si besoin
        )
    }

    // Redirige vers l'écran de connexion après le délai
    LaunchedEffect(Unit) {
        delay(5000) // Délai de 5 secondes
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true } // Supprime l'écran Splash de la pile
        }
    }
}
