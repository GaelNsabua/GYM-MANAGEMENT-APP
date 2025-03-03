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
import androidx.compose.material3.darkColorScheme
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
import com.example.gym_management_app.data.models.Member
import com.example.gym_management_app.viewmodel.MemberViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberListScreen(
    navController: NavController,
    memberViewModel: MemberViewModel,
    modifier: Modifier = Modifier
) {
    // Collecte l'état de la liste des membres à partir du ViewModel pour que l'UI se mette à jour automatiquement
    val members by memberViewModel.members.collectAsState()

    MaterialTheme(
        colorScheme = lightColorScheme(primary = Color.Blue),
        //colorScheme = darkColorScheme(primary = Color.Green)
    ) {
        Scaffold(
            //centrer le texte
            topBar = {
                TopAppBar(
                    title = { Text("Liste des membres")},
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary, // Couleur de fond
                        titleContentColor = MaterialTheme.colorScheme.onPrimary // Couleur du texte
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navController.navigate("addMember") }) {
                    Text("+") // Icône simple pour le bouton
                }
            }
        ) { paddingValues ->
            Column(modifier = modifier
                .wrapContentSize()
                .padding(paddingValues)) {
                Spacer(modifier = Modifier.height(16.dp))

                // Affichage de la liste défilante des membres
                LazyColumn{
                    items(members) { member ->
                        // Pour chaque membre, on affiche un élément de liste avec ses informations et un bouton pour la suppression
                        MemberItem(member = member, onDelete = { memberViewModel.deleteMember(member) })
                    }
                }
            }
        }
    }
}

@Composable
fun MemberItem(
    member: Member,
    onDelete: () -> Unit  // Callback pour la suppression du membre
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        // Utilisation d'un Box pour positionner le contenu et le bouton de suppression
        Box {
            // Colonne principale pour afficher les détails du membre
            Column(modifier = Modifier.padding(16.dp)) {
                // Affichage du nom du membre
                Text(text = member.name, style = MaterialTheme.typography.bodyLarge)
                // Affichage du contact
                Text(text = "Contact: ${member.contact}", style = MaterialTheme.typography.bodyMedium)
                // Affichage du statut, basé sur le champ isActive du modèle
                val statusText = if (member.isActive) "Actif" else "Inactif"
                Text(text = "Statut: $statusText", style = MaterialTheme.typography.bodySmall)
            }
            // Bouton de suppression positionné en haut à droite de la carte
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
