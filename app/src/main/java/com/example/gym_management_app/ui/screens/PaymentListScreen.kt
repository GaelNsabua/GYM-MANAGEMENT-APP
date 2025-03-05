package com.example.gym_management_app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gym_management_app.data.models.Payment
import com.example.gym_management_app.viewmodel.MemberViewModel
import com.example.gym_management_app.viewmodel.PaymentViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentListScreen(
    navController: NavController,
    paymentViewModel: PaymentViewModel,
    memberViewModel: MemberViewModel, // Ajout du MemberViewModel pour récupérer les membres
    modifier: Modifier = Modifier
) {
    // Collecte la liste des paiements depuis le PaymentViewModel
    val payments by paymentViewModel.payments.collectAsState()
    // Collecte la liste des membres depuis le MemberViewModel
    val members by memberViewModel.members.collectAsState()

    MaterialTheme(
        colorScheme = lightColorScheme(primary = Color.Blue),
        //colorScheme = darkColorScheme(primary = Color.Green)
    ) {
        Scaffold(
            //centrer le texte
            topBar = {
                TopAppBar(
                    title = { Text("Liste des paiements")},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary, // Couleur de fond
                        titleContentColor = MaterialTheme.colorScheme.onPrimary // Couleur du texte
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate("addPayment") }) {
                    Text("+") // Icône simple pour le bouton
                }
            }
        ) { paddingValues ->
            Column(modifier = modifier
                .wrapContentSize()
                .padding(paddingValues)
                .padding(16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))

                // Affiche la liste des paiements dans une LazyColumn
                LazyColumn{
                    items(payments) { payment ->
                        // Recherche le membre correspondant à payment.memberId dans la liste des membres
                        val memberName = members.find { it.id == payment.memberId }?.name ?: "Inconnu"
                        // Passe le nom du membre à PaymentItem au lieu de l'ID
                        PaymentItem(
                            payment = payment,
                            memberName = memberName,
                            onDelete = { paymentViewModel.deletePayment(payment) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentItem(
    payment: Payment,
    memberName: String, // Nouveau paramètre pour le nom du membre
    onDelete: () -> Unit
) {
    // Vérifie si le paiement est affiché dans une Card
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // Box permet de superposer le contenu principal et le bouton de suppression
        Box {
            Column(modifier = Modifier.padding(16.dp)) {
                // Affiche le montant du paiement
                Text(text = "Montant: ${payment.amount} $", style = MaterialTheme.typography.bodyLarge)
                // Affiche la date du paiement au format lisible
                Text(text = "Date: ${formatDates(payment.date)}", style = MaterialTheme.typography.bodyMedium)
                // Affiche le nom du membre associé au paiement (plutôt que l'ID)
                Text(text = "Membre: $memberName", style = MaterialTheme.typography.bodySmall)
            }
            // Bouton de suppression positionné en haut à droite de la Card
            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer le paiement"
                )
            }
        }
    }
}

/**
 * Convertit un timestamp (Long) en une chaîne de caractères au format "yyyy-MM-dd".
 */
fun formatDates(timestamp: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(timestamp))
}
