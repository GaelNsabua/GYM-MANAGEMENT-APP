package com.example.gym_management_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gym_management_app.data.models.Subscription
import com.example.gym_management_app.ui.components.BottomNavBar
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
    // Récupère la liste des abonnements depuis le ViewModel
    val subscriptions by subscriptionViewModel.subscriptions.collectAsState()

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Liste des abonnements", fontWeight = FontWeight.Bold, color = Color.White) },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White
                    ),
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                onClick = { navController.navigate("addSubscription") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter un abonnement", tint = Color.White)
            }

            },
            bottomBar = { BottomNavBar(navController, navController.currentDestination?.route) }
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .padding(paddingValues)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // Liste des abonnements
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(subscriptions) { subscription ->
                        SubscriptionItem(
                            subscription = subscription,
                            onDelete = { subscriptionViewModel.deleteSubscription(subscription) }
                        )
                    }
                }
            }
        }

}

@Composable
fun SubscriptionItem(
    subscription: Subscription,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Nom de l'abonnement
                Text(
                    text = subscription.type,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Prix de l'abonnement
                Text(
                    text = "Prix: ${subscription.price} $",
                    style = MaterialTheme.typography.bodyMedium
                )

                // Description de l'abonnement
                Text(
                    text = "Description",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = subscription.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            // Bouton de suppression
            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer l'abonnement",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}