package com.example.gym_management_app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gym_management_app.R
import com.example.gym_management_app.viewmodel.MemberViewModel
import com.example.gym_management_app.viewmodel.PaymentViewModel
import com.example.gym_management_app.viewmodel.SubscriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    memberViewModel: MemberViewModel = viewModel(),
    subscriptionViewModel: SubscriptionViewModel = viewModel(),
    paymentViewModel: PaymentViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val activeMembersCount = 120          // Exemple : nombre de membres actifs
    val expiredSubscriptionsCount = 15    // Exemple : nombre d'abonnements expirés
    val totalPayments = 100000             // Exemple : total des paiements en Fc

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tableau de bord", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Accueil") },
                    selected = navController.currentDestination?.route == "home",
                    onClick = { navController.navigate("home") }
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.people), contentDescription = "Rapports", modifier = Modifier.size(24.dp), tint = Color.Black) },
                    label = { Text("Membres") },
                    selected = navController.currentDestination?.route == "memberList",
                    onClick = { navController.navigate("memberList") }
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.list), contentDescription = "Rapports", modifier = Modifier.size(24.dp), tint = Color.Black)},
                    label = { Text("Abonnements") },
                    selected = navController.currentDestination?.route == "subscriptionList",
                    onClick = { navController.navigate("subscriptionList") }
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.money), contentDescription = "Rapports", modifier = Modifier.size(24.dp), tint = Color.Black)},
                    label = { Text("Paiements") },
                    selected = navController.currentDestination?.route == "paymentList",
                    onClick = { navController.navigate("paymentList") }
                )
                NavigationBarItem(
                    icon = { Icon(painter = painterResource(id = R.drawable.reports), contentDescription = "Rapports", modifier = Modifier.size(24.dp), tint = Color.Black)},
                    label = { Text("Rapports") },
                    selected = navController.currentDestination?.route == "reportScreen",
                    onClick = { navController.navigate("reportScreen") }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {navController.navigate("addMember")},
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter un membre")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            /* Message de bienvenue
            Text(
                text = "Bienvenue dans votre application de gestion de salle de sport",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
*/
            Spacer(modifier = Modifier.height(24.dp))

            // Section des statistiques
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatisticCard(
                    title = "Membres Actifs",
                    value = activeMembersCount.toString(),
                    icon = R.drawable.people,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                StatisticCard(
                    title = "Abonnements Expirés",
                    value = expiredSubscriptionsCount.toString(),
                    icon = R.drawable.warning,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            StatisticCard(
                title = "Total Paiements",
                value = "$totalPayments Fc",
                icon = R.drawable.moneys,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

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
}

@Composable
fun StatisticCard(
    title: String,
    value: String,
    icon: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(vertical = 4.dp)
            .height(120.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(

            painter = painterResource(id = icon),

            contentDescription = title,

            tint = MaterialTheme.colorScheme.primary,

            modifier = Modifier.size(32.dp)

        )
            Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(text = value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 4.dp
        )
    ) {
        Icon(icon, contentDescription = text, modifier = Modifier.size(ButtonDefaults.IconSize))
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(text.uppercase(), fontWeight = FontWeight.Bold)
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}