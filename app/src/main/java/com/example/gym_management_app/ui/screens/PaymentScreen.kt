package com.example.gym_management_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gym_management_app.data.models.Member
import com.example.gym_management_app.data.models.Payment
import com.example.gym_management_app.viewmodel.MemberViewModel
import com.example.gym_management_app.viewmodel.PaymentViewModel
import com.example.gym_management_app.viewmodel.SubscriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    paymentViewModel: PaymentViewModel,
    subscriptionViewModel: SubscriptionViewModel,
    memberViewModel: MemberViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val members by memberViewModel.members.collectAsState()
    var selectedMember by remember { mutableStateOf<Member?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val focusRequester = remember { FocusRequester() }
    var isDropdownExpanded by remember { mutableStateOf(false) }

        Scaffold(
            //centrer le texte
            topBar = {
                TopAppBar(
                    title = { Text("Effectuer un paiement")},
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
            Column(modifier = modifier
                .wrapContentSize()
                .padding(paddingValues)
                .padding(16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))

                // Sélection d'un membre
                var expanded by remember { mutableStateOf(false) }

                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = selectedMember?.name ?: "Sélectionner un membre")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        members.forEach { member ->
                            DropdownMenuItem(
                                text = { Text(member.name) },
                                onClick = {
                                    selectedMember = member
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Affichage automatique du montant
                // Observer la souscription du membre sélectionné
                val subscription = selectedMember?.let { member ->
                    subscriptionViewModel.getSubscriptionById(member.subscriptionId).collectAsState(initial = null).value
                }

                // Affichage automatique du montant en fonction de l'abonnement du membre
                val subscriptionPrice = subscription?.price ?: 0


                OutlinedTextField(
                    value = "$subscriptionPrice $",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Montant à payer") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.onSurface
                    )
                )

                if (errorMessage != null) {
                    Text(text = errorMessage!!, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }

                Button(
                    onClick = {
                        if (selectedMember == null) {
                            errorMessage = "Veuillez sélectionner un membre."
                        } else {
                            val payment = Payment(
                                memberId = selectedMember!!.id,
                                amount = subscriptionPrice as Double,
                                date = System.currentTimeMillis()
                            )
                            paymentViewModel.addPaymentAndUpdateStatus(payment)
                            Toast.makeText(context, "Paiement enregistré avec succès", Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Text("Enregistrer le paiement")
                }
            }
        }
}
