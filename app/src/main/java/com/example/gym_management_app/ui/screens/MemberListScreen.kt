package com.example.gym_management_app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
    val members by memberViewModel.members.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var filterStatus by remember { mutableStateOf("Tous") } // Actifs, Expirés, Tous
    var sortOption by remember { mutableStateOf("Date d'inscription") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Liste des membres") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
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
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            // Barre de recherche
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Rechercher un membre") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Recherche") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filtres Actifs/Inactifs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Tous", "Actifs", "Expirés").forEach { status ->
                    FilterChip(
                        selected = filterStatus == status,
                        onClick = { filterStatus = status },
                        label = { Text(status) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Menu déroulant pour le tri
            var expanded by remember { mutableStateOf(false) }
            Box {
                Button(onClick = { expanded = true }) {
                    Text("Trier par : $sortOption")
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("Date d'inscription", "Dernier paiement").forEach { option ->
                        DropdownMenuItem(text = { Text(option) }, onClick = {
                            sortOption = option
                            expanded = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Filtrage et tri des membres
            val filteredAndSortedMembers = members
                .filter { member ->
                    val subscription = subscriptionViewModel.getSubscriptionById(member.subscriptionId)
                        .collectAsState(initial = null).value

                    val matchesSearch = searchQuery.isBlank() ||
                            member.name.contains(searchQuery, ignoreCase = true) ||
                            (subscription?.type?.contains(searchQuery, ignoreCase = true) ?: false)

                    val matchesFilter = when (filterStatus) {
                        "Actifs" -> subscription?.endDate?.let { it > System.currentTimeMillis() } == true
                        "Expirés" -> subscription?.endDate?.let { it <= System.currentTimeMillis() } == true
                        else -> true
                    }

                    matchesSearch && matchesFilter
                }
                .sortedBy { member ->
                    when (sortOption) {
                        "Date d'inscription" -> member.registrationDate
                        else -> member.registrationDate
                    }
                }

            // Affichage de la liste
            LazyColumn {
                items(filteredAndSortedMembers) { member ->
                    MemberItem(
                        member = member,
                        subscriptionViewModel = subscriptionViewModel,
                        onDelete = { memberViewModel.deleteMember(member) },
                        onClick = { navController.navigate("memberDetail/${member.id}") }
                    )
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
    onClick: () -> Unit
) {
    var subscription by remember { mutableStateOf<Subscription?>(null) }

    LaunchedEffect(member.subscriptionId) {
        subscription = subscriptionViewModel.getSubscriptionForMember(member.subscriptionId)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }, // Permet de cliquer sur le membre pour voir les détails
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = member.name, style = MaterialTheme.typography.bodyLarge)
                Text(text = "Contact: ${member.contact}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Abonnement: ${subscription?.type ?: "Chargement..."}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Date inscription: ${formatDate(member.registrationDate)}", style = MaterialTheme.typography.bodyMedium)

                if (subscription != null) {
                    Text(
                        text = if (System.currentTimeMillis() > subscription!!.endDate) "Inactif" else "Actif",
                        color = if (System.currentTimeMillis() > subscription!!.endDate) Color.Red else Color.Green,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer le membre")
            }
        }
    }
}


