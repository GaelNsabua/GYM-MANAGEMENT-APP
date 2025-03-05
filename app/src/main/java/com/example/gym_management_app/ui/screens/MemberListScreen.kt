package com.example.gym_management_app.ui.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gym_management_app.data.models.Member
import com.example.gym_management_app.data.models.Subscription
import com.example.gym_management_app.viewmodel.MemberViewModel
import com.example.gym_management_app.viewmodel.SubscriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberListScreen(
    navController: NavController,
    memberViewModel: MemberViewModel,
    subscriptionViewModel: SubscriptionViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    // Collecte l'état de la liste des membres depuis le ViewModel
    val members by memberViewModel.members.collectAsState()

    MaterialTheme(
        colorScheme = lightColorScheme(primary = Color.Blue)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Liste des membres") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate("addMember") }) {
                    Text("+")
                }
            }
        ) { paddingValues ->
            Column(
                modifier = modifier
                    .wrapContentSize()
                    .padding(16.dp)
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(members) { member ->
                        // Ajout du onItemClick pour naviguer vers l'écran de détail
                        MemberItem(
                            member = member,
                            subscriptionViewModel = subscriptionViewModel,
                            onDelete = { memberViewModel.deleteMember(member) },
                            onItemClick = { navController.navigate("memberDetail/${member.id}") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MemberItem(
    member: Member,
    subscriptionViewModel: SubscriptionViewModel,
    onDelete: () -> Unit,
    onItemClick: () -> Unit // Nouveau paramètre pour le clic sur l'item
) {
    var subscription by remember { mutableStateOf<Subscription?>(null) }

    // Charger l'abonnement une seule fois par membre, lorsque member.subscriptionId change
    LaunchedEffect(key1 = member.subscriptionId) {
        subscription = subscriptionViewModel.getSubscriptionById(member.subscriptionId).value
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onItemClick() }, // Rendre la carte cliquable pour naviguer aux détails
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = member.name, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Contact: ${member.contact}", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = "Abonnement: ${subscription?.type ?: "Chargement..."}",
                    style = MaterialTheme.typography.bodyMedium
                )
                if (subscription != null) {
                    if (System.currentTimeMillis() > subscription!!.endDate) {
                        Text(text = "Inactif", color = Color.Red, style = MaterialTheme.typography.bodyMedium)
                    } else {
                        Text(text = "Actif", color = Color.Green, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Supprimer le membre"
                )
            }
        }
    }
}

