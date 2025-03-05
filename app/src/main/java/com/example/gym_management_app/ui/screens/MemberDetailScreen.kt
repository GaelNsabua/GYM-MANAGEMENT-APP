package com.example.gym_management_app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gym_management_app.data.models.Payment
import com.example.gym_management_app.data.models.Subscription
import com.example.gym_management_app.viewmodel.MemberViewModel
import com.example.gym_management_app.viewmodel.PaymentViewModel
import com.example.gym_management_app.viewmodel.SubscriptionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberDetailScreen(
    navController: NavController,
    memberId: Int,  // ID du membre à afficher
    memberViewModel: MemberViewModel = viewModel(),
    subscriptionViewModel: SubscriptionViewModel = viewModel(),
    paymentViewModel: PaymentViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Récupération de la liste des membres depuis le MemberViewModel
    val members by memberViewModel.members.collectAsState()
    // Recherche du membre dont l'ID correspond
    val member = members.find { it.id == memberId }

    // Récupération de la liste des paiements associés au membre (via son ID)
    val payments by paymentViewModel.getPaymentsForMember(memberId)
        .collectAsState(initial = emptyList())

    // Variable locale pour stocker les informations de l'abonnement
    var subscription by remember { mutableStateOf<Subscription?>(null) }
    // Charger l'abonnement uniquement lorsque le membre est disponible ou change
    LaunchedEffect(key1 = member?.subscriptionId) {
        member?.let {
            // Appel de la fonction suspendue dans le SubscriptionViewModel
            subscription = subscriptionViewModel.getSubscriptionById(it.subscriptionId).value
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Détails du Membre") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Affichage des informations du membre
            member?.let { m ->
                Text(text = "Nom: ${m.name}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Contact: ${m.contact}", style = MaterialTheme.typography.bodyLarge)

                // Si l'abonnement a été chargé, l'afficher
                subscription?.let { s ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Abonnement: ${s.type}", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = "Validité: ${formatDate(s.startDate)} - ${formatDate(s.endDate)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    // Indique si l'abonnement est actif ou expiré
                    if (System.currentTimeMillis() > s.endDate) {
                        Text(text = "Statut: Inactif", color = Color.Red, style = MaterialTheme.typography.bodyLarge)
                    } else {
                        Text(text = "Statut: Actif", color = Color.Green, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            } ?: run {
                // Affiche un message si le membre n'est pas trouvé
                Text("Membre introuvable", color = Color.Red)
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Historique des Paiements", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // Affichage de la liste des paiements associés à ce membre
            LazyColumn {
                items(payments) { payment ->
                    PaymentItem(payment = payment)
                }
            }
        }
    }
}

/**
 * Composable pour afficher un élément de paiement.
 */
@Composable
fun PaymentItem(payment: Payment) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Montant: ${payment.amount} ₯CFA", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Date: ${formatsDate(payment.date)}", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

/**
 * Formate un timestamp (Long) en une date au format "yyyy-MM-dd".
 */
fun formatsDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(timestamp))
}
