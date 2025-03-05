package com.example.gym_management_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gym_management_app.data.models.Subscription
import com.example.gym_management_app.viewmodel.SubscriptionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSubscriptionScreen(
    navController: NavController,
    subscriptionViewModel: SubscriptionViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // États pour les entrées utilisateur
    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf<Date?>(null) }
    var endDate by remember { mutableStateOf<Date?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // États pour les Date Pickers
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    MaterialTheme(
        colorScheme = lightColorScheme(primary = Color.Blue)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Ajouter un abonnement") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val priceValue = price.toIntOrNull()
                        val startMillis = startDate?.time
                        val endMillis = endDate?.time

                        // Validation des champs
                        if (name.isBlank()) {
                            errorMessage = "Le nom de l'abonnement ne peut pas être vide."
                        } else if (priceValue == null || priceValue <= 0) {
                            errorMessage = "Le prix doit être un nombre positif."
                        } else if (startMillis == null) {
                            errorMessage = "Veuillez sélectionner une date de début."
                        } else if (endMillis == null || endMillis <= startMillis) {
                            errorMessage = "Date de fin invalide ou antérieure à la date de début."
                        } else {
                            // Création et ajout de l'abonnement
                            val subscription = Subscription(
                                type = name,
                                price = priceValue.toDouble(),
                                startDate = startMillis,
                                endDate = endMillis
                            )
                            subscriptionViewModel.addSubscription(subscription)

                            // Affichage d'un message de confirmation
                            Toast.makeText(context, "Abonnement ajouté avec succès", Toast.LENGTH_SHORT).show()

                            // Retour à l'écran précédent
                            navController.popBackStack()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Ajouter")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Champ pour le nom de l'abonnement
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom de l'abonnement") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Champ pour le prix
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it.filter { char -> char.isDigit() } },
                    label = { Text("Prix ($)") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Bouton pour sélectionner la date de début
                Button(
                    onClick = { showStartDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (startDate != null) {
                            "Date de début : ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(startDate!!)}"
                        } else {
                            "Sélectionner la date de début"
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bouton pour sélectionner la date de fin
                Button(
                    onClick = { showEndDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (endDate != null) {
                            "Date de fin : ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(endDate!!)}"
                        } else {
                            "Sélectionner la date de fin"
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Affichage des erreurs
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }

    // Date Picker pour la date de début
    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            onDateSelected = { date ->
                startDate = date
                showStartDatePicker = false
            }
        )
    }

    // Date Picker pour la date de fin
    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            onDateSelected = { date ->
                endDate = date
                showEndDatePicker = false
            }
        )
    }
}

@Composable
fun DatePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (Date) -> Unit
) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePicker = android.app.DatePickerDialog(
        LocalContext.current,
        { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }.time
            onDateSelected(selectedDate)
        },
        year,
        month,
        day
    )

    datePicker.show()
}