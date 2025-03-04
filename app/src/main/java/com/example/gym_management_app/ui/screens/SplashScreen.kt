package com.example.gym_management_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.gym_management_app.R

@Composable
fun SplashScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Ajoutez une image de fond pour le Splash Screen
        Image(
            painter = painterResource(id = R.drawable.back), // image de fond
            contentDescription = "Splash Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

    // Redirige vers l'écran de connexion après le délai
    LaunchedEffect(Unit) {
        delay(5000) // delai de 5 secondes d'affichage
        navController.navigate("login") // Redirige vers l'écran de connexion
    }
}