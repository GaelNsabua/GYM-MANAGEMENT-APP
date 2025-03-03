package com.example.gym_management_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    MaterialTheme(
        colorScheme = lightColorScheme(primary = Color.Blue),
        //colorScheme = darkColorScheme(primary = Color.Green)
    ) {
        Scaffold(
            //centrer le texte
            topBar = {
                TopAppBar(
                    title = { Text("Ajouter un abonnement")},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary, // Couleur de fond
                        titleContentColor = MaterialTheme.colorScheme.onPrimary // Couleur du texte
                    )
                )
            },
        ) { paddingValues ->
            Column(modifier = modifier
                    .wrapContentSize()
                    .padding(paddingValues)
                    .padding(16.dp)) {

                Spacer(modifier = Modifier.height(16.dp))
                // Input field for subscription name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom de l'abonnement") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Input field for subscription price, allowing only digits
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it.filter { char -> char.isDigit() } },
                    label = { Text("Prix (₯CDF)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Input field for subscription start date
                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Date de début (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Input field for subscription end date
                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("Date de fin (YYYY-MM-DD)") },
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
                        val startMillis = parseDate(startDate)
                        val endMillis = parseDate(endDate)

                        // Validation checks before adding the subscription
                        if (name.isBlank()) {
                            errorMessage = "Le nom de l'abonnement ne peut pas être vide."
                        } else if (priceValue == null || priceValue <= 0) {
                            errorMessage = "Le prix doit être un nombre positif."
                        } else if (startMillis == null) {
                            errorMessage = "Date de début invalide."
                        } else if (endMillis == null || endMillis <= startMillis) {
                            errorMessage = "Date de fin invalide ou antérieure à la date de début."
                        } else {
                            // Create and add a new subscription
                            val subscription = Subscription(type = name, price = priceValue.toDouble(), startDate = startMillis, endDate = endMillis)
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

/**
 * Parses a date string (YYYY-MM-DD) into a timestamp (Long).
 * Returns null if the date is invalid.
 */
fun parseDate(date: String): Long? {
    return try {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formatter.parse(date)?.time
    } catch (e: Exception) {
        null
    }
}


