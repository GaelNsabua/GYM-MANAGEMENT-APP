package com.example.gym_management_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gym_management_app.R
import com.example.gym_management_app.data.models.Member
import com.example.gym_management_app.data.models.Subscription
import com.example.gym_management_app.ui.components.BottomNavBar
import com.example.gym_management_app.viewmodel.MemberViewModel
import com.example.gym_management_app.viewmodel.SubscriptionViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    var filterStatus by remember { mutableStateOf("Tous") }
    var sortOption by remember { mutableStateOf("Date d'inscription") }

    val selectedTabIndex = when (filterStatus) {
        "Actifs" -> 1
        "Expirés" -> 2
        else -> 0
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Liste des membres", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("addMember") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter un membre", tint = Color.White)
            }
        },
        bottomBar = { BottomNavBar(navController, navController.currentDestination?.route) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(paddingValues)
        ) {
            // Barre de recherche avec bouton de filtre
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    IconButton(
                        onClick = { expanded = true },
                        // Taille de l'icône pour un bon visuel
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter),
                            contentDescription = "Filtrer",
                            modifier = Modifier.size(40.dp) // Taille de l'icône
                        )
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

                Spacer(modifier = Modifier.width(8.dp)) // Ajoute un espace entre la barre de recherche et l'icône

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Rechercher un membre") },
                    trailingIcon = { Icon(Icons.Default.Search, contentDescription = "Recherche") },
                    modifier = Modifier
                        .weight(1f),
                    maxLines = 1,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        focusedBorderColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Onglets pour filtrer les membres
            TabRow(
                selectedTabIndex = when (filterStatus) {
                    "Actifs" -> 1
                    "Expirés" -> 2
                    else -> 0
                },
                indicator = { tabPositions ->
                    Box(
                        Modifier
                            .tabIndicatorOffset(tabPositions[selectedTabIndex])
                            .height(3.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.onSurface)
                    )
                }
            ) {
                listOf("Tous", "Actifs", "Expirés").forEachIndexed { index, status ->
                    Tab(
                        selected = filterStatus == status,
                        onClick = { filterStatus = status },
                        text = { Text(status) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Filtrage et tri des membres
            val filteredAndSortedMembers = members
                .filter { member ->
                    val matchesSearch = searchQuery.isBlank() ||
                            member.name.contains(searchQuery, ignoreCase = true) ||
                            (member?.contact?.contains(searchQuery, ignoreCase = true) ?: false)

                    val matchesFilter = when (filterStatus) {
                        "Actifs" -> member?.endDate?.let { it > System.currentTimeMillis() } == true
                        "Expirés" -> member?.endDate?.let { it <= System.currentTimeMillis() } == true
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

            // Affichage de la liste avec des séparateurs
            LazyColumn {
                items(filteredAndSortedMembers) { member ->
                    MemberItem(
                        member = member,
                        subscriptionViewModel = subscriptionViewModel,
                        onDelete = { memberViewModel.deleteMember(member) },
                        onClick = { navController.navigate("memberDetail/${member.id}") }
                    )
                    HorizontalDivider() // Séparateur entre les éléments de la liste
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

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            Text(text = member.name, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Contact: ${member.contact}", style = MaterialTheme.typography.bodyMedium)
            Text(
                text = "Abonnement: ${subscription?.type ?: "Chargement..."}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Date inscription: ${formatDate(member.registrationDate)}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = if (System.currentTimeMillis() > member!!.endDate) "Inactif" else "Actif",
                color = if (System.currentTimeMillis() > member!!.endDate) Color.Red else Color.Green,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        IconButton(
            onClick = onDelete,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Delete, contentDescription = "Supprimer le membre")
        }
    }
}

// Fonction utilitaire pour formater un timestamp en date lisible
fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}
