package com.example.gym_management_app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.gym_management_app.viewmodel.MemberViewModel
import com.example.gym_management_app.viewmodel.PaymentViewModel
import com.example.gym_management_app.viewmodel.SubscriptionViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    memberViewModel: MemberViewModel,
    subscriptionViewModel: SubscriptionViewModel,
    paymentViewModel: PaymentViewModel,
    modifier: Modifier = Modifier
) {
    // Pour l'instant, nous utilisons des données fictives pour les statistiques.
    // Dans une version réelle, ces valeurs proviendraient de vos ViewModels.
    val activeMembersCount = 120          // Exemple : nombre de membres actifs
    val expiredSubscriptionsCount = 15    // Exemple : nombre d'abonnements expirés
    val totalPayments = 100000             // Exemple : total des paiements en CFA

    var cardgreen by remember { mutableStateOf(Color.Red) }
    var cardblue by remember { mutableStateOf(Color.Blue) }
    var cardorange by remember { mutableStateOf(Color.Magenta) }

    // Structure principale de l'écran : une colonne qui occupe tout l'espace et possède un padding.
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween  // Espace entre la section du haut et celle du bas
    ) {
        // SECTION DU HAUT : Message de bienvenue et statistiques
        Column {
            // Message de bienvenue centré
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Bienvenue dans Gym-management",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

                // Carte pour le nombre de membres actifs
                StatisticCard(
                    title = "Membres Actifs",
                    color = cardgreen,
                    value = activeMembersCount.toString()
                )
                // Carte pour le nombre d'abonnements expirés
                StatisticCard(
                    title = "Abonnements Expirés",
                    color = cardblue,
                    value = expiredSubscriptionsCount.toString()
                )
                // Carte pour le total des paiements
                StatisticCard(
                    title = "Total Paiements",
                    color = cardorange,
                    value = "$totalPayments $"
                )
        }

        // SECTION DU BAS : Boutons de navigation vers les différentes fonctionnalités
        Column {
            Button(
                onClick = { navController.navigate("memberList") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text("Gestion des Membres")
            }
            Button(
                onClick = { navController.navigate("subscriptionList") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text("Gestion des Abonnements")
            }
            Button(
                onClick = { navController.navigate("paymentList") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text("Paiements")
            }
            Button(
                onClick = { navController.navigate("reportScreen") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text("Rapports")
            }
        }
    }
}

/**
 * Composable qui affiche une carte de statistique.
 * @param title Titre de la statistique (ex. « Membres Actifs »)
 * @param value Valeur de la statistique (ex. « 120 »)
 */
@Composable
fun StatisticCard(title: String, color: Color, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = color, // Couleur de fond de la carte
            contentColor = Color.White  // Couleur du texte ou contenu
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // Disposition en colonne pour centrer le contenu dans la carte
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Text(text = value, style = MaterialTheme.typography.titleLarge)
        }
    }
}



