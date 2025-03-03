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
import androidx.navigation.NavController
import com.example.gym_management_app.data.models.Subscription
import com.example.gym_management_app.viewmodel.SubscriptionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionListScreen(
    navController: NavController,
    subscriptionViewModel: SubscriptionViewModel,
    modifier: Modifier = Modifier
) {
    // Récupère la liste des abonnements depuis le ViewModel et la convertit en état pour la recomposition
    val subscriptions by subscriptionViewModel.subscriptions.collectAsState()

    MaterialTheme(
        colorScheme = lightColorScheme(primary = Color.Blue),
        //colorScheme = darkColorScheme(primary = Color.Green)
    ) {
        Scaffold(
            //centrer le texte
            topBar = {
                TopAppBar(
                    title = { Text("Liste des abonnements")},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary, // Couleur de fond
                        titleContentColor = MaterialTheme.colorScheme.onPrimary // Couleur du texte
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate("addSubscription") }) {
                    Text("+") // Icône simple pour le bouton
                }
            }
        ) { paddingValues ->
            Column(modifier = modifier
                    .wrapContentSize()
                    .padding(paddingValues)
                    .padding(16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))

                // Affiche la liste des abonnements sous forme de LazyColumn (liste défilable)
                LazyColumn {
                    items(subscriptions) { subscription ->
                        // Pour chaque abonnement, on affiche un item avec un bouton de suppression
                        SubscriptionItem(
                            subscription = subscription,
                            // Lorsque le bouton de suppression est cliqué, on appelle la fonction delete du ViewModel
                            onDelete = { subscriptionViewModel.deleteSubscription(subscription) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SubscriptionItem(
    subscription: Subscription,
    onDelete: () -> Unit // Callback pour la suppression de l'abonnement
) {
    // Détermine si l'abonnement est expiré en comparant la date actuelle avec la date de fin
    val isExpired = System.currentTimeMillis() > subscription.endDate

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        // Change la couleur de fond en fonction de l'état de l'abonnement (expiré ou actif)
        colors = CardDefaults.cardColors(
            containerColor = if (isExpired) Color.Gray else Color.White
        )
    ) {
        // Utilisation d'un Box pour superposer le contenu et le bouton de suppression
        Box{
            // Contenu principal affiché dans une colonne
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Affiche le nom de l'abonnement
                Text(text = subscription.type, style = MaterialTheme.typography.bodyLarge)
                // Affiche le prix de l'abonnement
                Text(text = "Prix: ${subscription.price} ₯CDF", style = MaterialTheme.typography.bodyMedium)
                // Affiche la période de validité en convertissant les timestamps en dates lisibles
                Text(
                    text = "Période: ${formatDate(subscription.startDate)} - ${formatDate(subscription.endDate)}",
                    style = MaterialTheme.typography.bodySmall
                )

                // Affiche l'état de l'abonnement (expiré ou actif)
                if (isExpired) {
                    Text(text = "Expiré", color = Color.Red, style = MaterialTheme.typography.bodyMedium)
                } else {
                    Text(text = "Actif", color = Color.Green, style = MaterialTheme.typography.bodyMedium)
                }
            }
            // Bouton de suppression positionné en haut à droite de la carte
            IconButton(
                onClick = { onDelete() }, // Appelle la fonction de suppression passée en callback
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                // Icône de suppression (poubelle)
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer l'abonnement"
                )
            }
        }
    }
}

// Fonction utilitaire pour formater un timestamp (Long) en une date au format "yyyy-MM-dd"
fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

