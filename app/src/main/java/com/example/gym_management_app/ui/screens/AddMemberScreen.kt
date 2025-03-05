package com.example.gym_management_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gym_management_app.data.models.Member
import com.example.gym_management_app.data.models.Subscription
import com.example.gym_management_app.viewmodel.MemberViewModel
import com.example.gym_management_app.viewmodel.SubscriptionViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemberScreen(
    navController: NavController,
    memberViewModel: MemberViewModel = viewModel(),
    subscriptionViewModel: SubscriptionViewModel = viewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var selectedSubscription by remember { mutableStateOf<Subscription?>(null) }
    val subscriptions by subscriptionViewModel.subscriptions.collectAsState()

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }

    MaterialTheme(
        colorScheme = lightColorScheme(primary = Color.Blue),
        //colorScheme = darkColorScheme(primary = Color.Green)
    ) {
        Scaffold(
            //centrer le texte
            topBar = {
                TopAppBar(
                    title = { Text("Ajouter un Membre")},
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary, // Couleur de fond
                        titleContentColor = MaterialTheme.colorScheme.onPrimary // Couleur du texte
                    )
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues)
            ) {

                // Champ Nom
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nom") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Champ Contact
                OutlinedTextField(
                    value = contact,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) { // Vérifie que tous les caractères sont des chiffres
                            contact = it
                        }
                    },
                    label = { Text("Contact") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, // Clavier numérique
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sélection d'un abonnement
                var expanded by remember { mutableStateOf(false) }

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = selectedSubscription?.type ?: "Sélectionner un abonnement")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        subscriptions.forEach { subscription ->
                            DropdownMenuItem(
                                text = { Text(subscription.type) },
                                onClick = {
                                    selectedSubscription = subscription
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Affichage des erreurs avec un Snackbar
                errorMessage?.let { message ->
                    LaunchedEffect(snackbarHostState) {
                        snackbarHostState.showSnackbar(message)
                    }
                }

                SnackbarHost(hostState = snackbarHostState)

                // Bouton Ajouter
                Button(
                    onClick = {
                        if (name.isBlank()) {
                            errorMessage = "Le nom est obligatoire"
                        } else if (contact.isBlank()) {
                            errorMessage = "Le contact est obligatoire"
                        } else if (selectedSubscription == null) {
                            errorMessage = "Veuillez sélectionner un abonnement"
                        } else {
                            // Vérifier si un membre avec ce subscriptionId existe déjà
                            coroutineScope.launch {
                                val existingMember = memberViewModel.getMemberBySubscriptionId(selectedSubscription!!.id)
                                if (existingMember != null) {
                                    errorMessage = "Un membre est déjà associé à cet abonnement."
                                } else {
                                    // Si non, créer et insérer le nouveau membre
                                    val newMember = Member(
                                        name = name,
                                        contact = contact,
                                        subscriptionId = selectedSubscription!!.id,
                                        registrationDate = System.currentTimeMillis(),
                                        isActive = true
                                    )
                                    memberViewModel.addMember(newMember)
                                    Toast.makeText(context, "Membre ajouté avec succès", Toast.LENGTH_SHORT).show()
                                    navController.popBackStack()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Ajouter Membre")
                }
            }
        }
    }
}




