package com.example.gym_management_app.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
    val members by memberViewModel.members.collectAsState(initial = emptyList())
    var selectedMember by remember { mutableStateOf<Member?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Effectuer un paiement", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        // Sélection du membre
        ExposedDropdownMenuBox(
            expanded = members.isNotEmpty(),
            onExpandedChange = {}
        ) {
            OutlinedTextField(
                value = selectedMember?.name ?: "Sélectionner un membre",
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )
            DropdownMenu(expanded = members.isNotEmpty(), onDismissRequest = {}) {
                members.forEach { member ->
                    DropdownMenuItem(
                        text = { Text(member.name) },
                        onClick = { selectedMember = member }
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
            value = "$subscriptionPrice CFA",
            onValueChange = {},
            readOnly = true,
            label = { Text("Montant à payer") },
            modifier = Modifier.fillMaxWidth()
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
