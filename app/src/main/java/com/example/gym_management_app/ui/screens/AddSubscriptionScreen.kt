package com.example.gym_management_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gym_management_app.data.models.Subscription
import com.example.gym_management_app.viewmodel.SubscriptionViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubscriptionScreen(
    navController: NavController,
    subscriptionViewModel: SubscriptionViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Mutable states for user inputs
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    MaterialTheme(
        colorScheme = lightColorScheme(primary = Color.Blue),
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Ajouter un abonnement") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .wrapContentSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                // Input field for subscription name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom de l'abonnement") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Input field for subscription price
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it.filter { char -> char.isDigit() } },
                    label = { Text("Prix ($)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Display error message if validation fails
                if (errorMessage != null) {
                    Text(text = errorMessage!!, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }

                // Button to add a new subscription
                Button(
                    onClick = {
                        val priceValue = price.toIntOrNull()
                        val startMillis = System.currentTimeMillis() // Date actuelle
                        val endMillis = startMillis + (7L * 24L * 60L * 60L * 1000L) // +30 jours

                        // Validation checks
                        if (name.isBlank()) {
                            errorMessage = "Le nom de l'abonnement ne peut pas être vide."
                        } else if (priceValue == null || priceValue <= 0) {
                            errorMessage = "Le prix doit être un nombre positif."
                        } else {
                            // Create and add a new subscription
                            val subscription = Subscription(
                                type = name,
                                price = priceValue.toDouble(),
                                startDate = startMillis,
                                endDate = endMillis
                            )
                            subscriptionViewModel.addSubscription(subscription)

                            // Show confirmation message
                            Toast.makeText(context, "Abonnement ajouté avec succès", Toast.LENGTH_SHORT).show()

                            // Navigate back to the previous screen
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Text("Ajouter")
                }
            }
        }
    }
}



